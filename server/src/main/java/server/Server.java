package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLTableControler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsContext;
import modules.AuthData;
import modules.GameData;
import modules.User;
import services.AccountService;
import services.DeleteService;
import services.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server {


    private final Javalin server;
    AccountService accountService = new AccountService();
    GameService gameService = new GameService();
    private boolean isRunning = false;
    Map<String, ArrayList<WsContext>> Notification_map;




    public Server() {
        try {
            SQLTableControler.initialize();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        server = Javalin.create(config -> config.staticFiles.add("web"));
//        server.start();
        server.delete("db", this::deleteall);

        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGames);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
        server.ws("echo/{gamename}",this::echo);
        server.ws("ws",this::connectGame);
        // Register your endpoints and exception handlers here.

    }
    private void notificationSender(String gameID, ServerMessage serverMessage){
        Gson gson = new Gson();
        for(var context:Notification_map.get(gameID)){
            System.out.println(context);
            String sendingMessage = gson.toJson(serverMessage);
            context.send(sendingMessage);
        }
    }

    private void notificationSenderminusOne(String gameID, ServerMessage message, WsContext ctx){
        Gson gson = new Gson();
        for(var context:Notification_map.get(gameID)){
            System.out.println(context);
            if(!context.equals(ctx)) {
                String sendingMessage = gson.toJson(message);
                context.send(sendingMessage);
            }
        }
    }
    private void connectGame(WsConfig ws){
        ws.onConnect(ctx -> {

            System.out.println("Websocket connected");
        });
        ws.onMessage(ctx -> {
            UserGameCommand command = null;
            Gson gson = new Gson();
            String message = ctx.message();
            System.out.println(message);
            var ctxMap = gson.fromJson(message,Map.class);




            if ( "MAKE_MOVE".equals(ctxMap.get("commandType"))) {
                // this is if the command type is make move. It's different because it
                // implements a different class
                var moveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);

                if (!accountService.checkAuth(moveCommand.getAuthToken())) {
                    System.out.println(ctx);
                    ServerMessage serverMessage = new ErrorMessage(
                            ServerMessage.ServerMessageType.ERROR,
                            "{\"error\":\"You aren't authorized to make this connection connected\"}"
                    );
                    String sendingMessage = new Gson().toJson(serverMessage);
                    ctx.send(sendingMessage);
                }
                var move = moveCommand.getMove();
                GameData gameData = gameService.inDatabaseID(moveCommand.getGameID());
                ChessGame chessGame = gameData.game();
                try{
                    String username = accountService.getUsernameFromAuth(moveCommand.getAuthToken());
                    var playersColor = (Objects.equals(username, gameData.whiteUsername())) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    var board = chessGame.getBoard();
                    if(playersColor!= board.getColor(move.getStartPosition())){
                        throw new InvalidMoveException("");
                    }
                    chessGame.makeMove(move);
                    GameData newGameData = new GameData(gameData.gameID(),gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(),chessGame);
                    var games = gameService.getGames();

                    for(var game:games){
                        if(game.gameID()==moveCommand.getGameID()){
                            String moversUsername = accountService.getUsernameFromAuth(moveCommand.getAuthToken());
                            if(!Objects.equals(moversUsername, game.whiteUsername()) && !Objects.equals(moversUsername, game.blackUsername())){
                                throw new InvalidMoveException("");
                            }

                            gson = new Gson();
                            String gameJson = gson.toJson(game);
                            LoadGameMessage returnmessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameJson);
                            notificationSender(String.valueOf(game.gameID()),returnmessage);
                            //"{"notification": playerColor +" has made a move"}"
                            gameService.updateBoard(newGameData);
                            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"{\"notification\":\"White has made a move\"}");
                            notificationSenderminusOne(String.valueOf(game.gameID()),notificationMessage,ctx);

//                            ctx.send(gson.toJson(returnmessage));
                        }
                    }
                }
                catch (InvalidMoveException e){
                    System.out.println("that was a bad move G");
                    //{"error":"The move that you tried to make was invalid"}
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"The move that you tried to make was invalid\"}");
                    ctx.send(gson.toJson(errorMessage));

                }

                // do the make move logic
            }
            command = gson.fromJson(ctx.message(), UserGameCommand.class);
            if(!accountService.checkAuth(command.getAuthToken())){
                System.out.println(ctx);
                ErrorMessage serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You aren't authorized to make this connection connected\"}");
                String sendingMessage = new Gson().toJson(serverMessage);
                ctx.send(sendingMessage);
                return;
            }
            if(command.getCommandType()== UserGameCommand.CommandType.LEAVE){
                GameData gameData = gameService.inDatabaseID(command.getGameID());
                NotificationMessage serverMessage;
                if (Objects.equals(gameData.whiteUsername(), command.getUsername())){
                    gameService.assignColor(null, ChessGame.TeamColor.WHITE,command.getGameID());
                }

                else if (Objects.equals(gameData.blackUsername(), command.getUsername())) {
                    gameService.assignColor(null, ChessGame.TeamColor.BLACK, command.getGameID());
                }

                serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"{\"notification\":\""+command.getUsername()+" left the game\"}");
                notificationSender(String.valueOf(command.getGameID()),serverMessage);
                Notification_map.get(command.getGameID()).removeIf(c-> c.sessionId().equals(ctx.sessionId()));
            }

            else if (command.getCommandType()== UserGameCommand.CommandType.RESIGN){
                var games = gameService.getGames();
                ChessGame.TeamColor winningColor = null;
                for(var game:games){

                    if(game.gameID()== command.getGameID()) {
                        if(game.game().hasWon()!=null){
                            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You cant resign the gam\"}");
                            ctx.send(gson.toJson(errorMessage));
                        }
                        var username = accountService.getUsernameFromAuth(command.getAuthToken());
                        if (Objects.equals(game.whiteUsername(), username)) {
                            winningColor = ChessGame.TeamColor.BLACK;
                        }
                        else if(Objects.equals(game.blackUsername(), username)) {
                            winningColor = ChessGame.TeamColor.WHITE;
                        }
                        else {
                            //{"error":"You arnt authroized to resign"}
                            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You arnt authroized to resign\"}");
                            ctx.send(gson.toJson(errorMessage));
                        }
                        if (game.gameID() == command.getGameID()) {
                            ChessGame chessGame = game.game();
                            chessGame.winSetter(winningColor);
                            GameData updatedBoard = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
                            gameService.updateBoard(updatedBoard);

                        }
                    }
                }
                String winnerString = (winningColor== ChessGame.TeamColor.WHITE) ? "White":"Black";
                ServerMessage declareWinner = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "{\"notification\":\""+winnerString + " has won the game!!!!\"}");
//                notificationSenderminusOne(String.valueOf(command.getGameID()),declareWinner,ctx);
                // {
//                ServerMessage gameOver = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,  "{\"notification\":\"The game is over\"}");
                notificationSender(String.valueOf(command.getGameID()),declareWinner);
            }

            else if (command.getCommandType() == UserGameCommand.CommandType.LOAD_GAME){
                var games = gameService.getGames();
                for(var game:games){
                    if(game.gameID()==command.getGameID()){
                        gson = new Gson();
                        String gameJson = gson.toJson(game);
                        LoadGameMessage returnmessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameJson);
                        ctx.send(gson.toJson(returnmessage));
                    }
                }
            }

            else if(command.getCommandType() == UserGameCommand.CommandType.CONNECT){
                String gameID = String.valueOf(command.getGameID());
                boolean gameExists = gameService.checkGameID(command.getGameID());
                if(!gameExists) { gson = new Gson();
                    //"{"error":"Your gameID is bad please try again"}"
                    ErrorMessage returnmessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "{\"error\":\"Your gameID is bad please try again\"}");
                    ctx.send(gson.toJson(returnmessage));
                }
                else{
                    System.out.println("a connection was made to " + gameID);
                    Notification_map.computeIfAbsent(gameID, k -> new ArrayList<>());

                    ctx.enableAutomaticPings();
                    String playerName = command.getUsername();
                    NotificationMessage serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "{\"notification\":\"" + playerName + " connected as white\"}");
                    notificationSender(gameID, serverMessage);
                    Notification_map.get(gameID).add(ctx);
                    var games = gameService.getGames();
                    boolean gameIDfound = false;
                    for (var game : games) {
                        if (game.gameID() == command.getGameID()) {
                            gson = new Gson();
                            String gameJson = gson.toJson(game);
                            LoadGameMessage returnmessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
                            ctx.send(gson.toJson(returnmessage));
                            System.out.println(returnmessage);
                        }
                    }
                }
            }



        });
        ws.onClose(ctx -> {System.out.println("Websocket closed");
        });
    }

    private void echo(WsConfig ws) {
        ws.onConnect(ctx -> {
            String gamename = ctx.pathParam("gamename");
            System.out.println("a connection was made to " +gamename);
            Notification_map.computeIfAbsent(gamename, k -> new ArrayList<>());
            Notification_map.get(gamename).add(ctx);
            ctx.enableAutomaticPings();
            System.out.println("Websocket connected");
            for(var context:Notification_map.get(gamename)){
                context.send("someone else connected");
            }
        });
        ws.onMessage(ctx -> ctx.send("WebSocket response:" + ctx.message()));
        ws.onClose(_ -> System.out.println("Websocket closed"));
    }


    private void joinGame(Context ctx) {
        var serializer = new Gson();
        System.out.println("joingame was made");
        //validates auth
        try {
            if (!validateAuth(ctx)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: You can't make that request\" }");
                return;
            }
            String username;
            username = accountService.getUsernameFromAuth(ctx.header("authorization"));
            var req = serializer.fromJson(ctx.body(), Map.class);
            if (req.get("playerColor") == null || req.get("gameID") == null) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: bad request\" }");
                return;
            }
            ChessGame.TeamColor joinColor;
            String joinstring = req.get("playerColor").toString().toLowerCase().trim();

            if (Objects.equals(joinstring, "white")) {
                joinColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(joinstring, "black")) {
                joinColor = ChessGame.TeamColor.BLACK;
            } else {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: playerColor was wrong\" }");
                return;
            }
//            switch (joinstring) {
//                case "white" -> joinColor = ChessGame.TeamColor.WHITE;
//                case "black" -> joinColor = ChessGame.TeamColor.BLACK;
//                default -> {
//                    ctx.status(400);
//                    ctx.result("{ \"message\": \"Error: playerColor was wrong\" }");
//                    return;
//                }
//            }

            int gameID = ((Double) req.get("gameID")).intValue();
            if (!gameService.checkGameID(gameID)) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: The game that you are trying to join dosn't exist\" }");
                return;
            }
            if (!gameService.assignColor(username, joinColor, gameID)) {
                ctx.status(403);
                ctx.result("{ \"message\": \"Error: The color is already taken\" }");
                return;
            }
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: There was a server error\" }\n");
            return;
        }
    }
    //makes sure that the game id is valid

    //checks joincolor


    private void deleteall(Context ctx) {
        var serializer = new Gson();
        boolean deleteSuccess;
        try {
            DeleteService.deleteAll();

            ctx.status(200);
            ctx.result("{}");
        } catch (SQLException | DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e + "\" }\n");
            return;
        }
    }

    private boolean validateAuth(Context ctx) throws DataAccessException {
        var authToken = ctx.header("authorization");

        return accountService.checkAuth(authToken);
    }

    private void createGame(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        if (req.get("gameName") == null) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: There was a problem createing your game\" }");
            return;
        }
        try {
            if (!validateAuth(ctx)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: unauthorized\" }");
                return;
            }
            GameData gameObject;
            gameObject = gameService.gameDataGenorator((String) req.get("gameName"));

            if (gameObject == null) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: That game name is already taken\" }");
                return;
            }
            String json;
            json = String.format("{ \"gameID\": \"%d\" }", gameObject.gameID());
//        String json ="{ \"gameID\": 1234 }";
            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(json);
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e + "\" }\n");
            return;
        }

    }

    private void listGames(Context ctx) {
        var serializer = new Gson();
        try {
            if (!validateAuth(ctx)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: unauthorized\" }");
                return;
            }

            ArrayList<GameData> gameList;

            gameList = gameService.getGames();

            Map<String, Object> response = new HashMap<>();
            response.put("games", gameList);
            ctx.status(200);
            System.out.println(serializer.toJson(response));
            ctx.result(serializer.toJson(response));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: There was a database error\" }\n");
            return;
        }
    }

    private void logout(Context ctx) {
        var serializer = new Gson();
        var authToken = ctx.header("authorization");
        try {
            if (!accountService.checkAuth(authToken)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: unauthorized\" }\n");
                return;
            }
            accountService.removeAuth(authToken);


            if (accountService.checkAuth(authToken)) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: (description of error)\" }\n");
                return;
            }

            // see if the auth token is valid
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e + "\" }\n");
            return;
        }
    }

    private void login(Context ctx) {
        try {
            var serializer = new Gson();
            var req = serializer.fromJson(ctx.body(), Map.class);
            // if either the username or password is null
            if (req.get("username") == null || req.get("password") == null) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: Your username or password was incorrect\" }");
                return;
            }
            User userData = new User(req.get("username").toString(), req.get("password").toString(), "");

            if (!accountService.checkUsername(userData.username())) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: Your username was incorrect\" }");
                return;
            }


            if (!accountService.checkPassword(userData.password(), userData)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: You had a wrong Password\" }");
                return;
            }

            AuthData authData = null;

            authData = accountService.authDataGenorator(userData.username());


            var resp = serializer.toJson(authData);
            ctx.status(200);
            ctx.result(resp);
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e + "\" }\n");
            return;
        }
    }

    private void register(Context ctx) {
        try {
            var serializer = new Gson();
            var req = serializer.fromJson(ctx.body(), Map.class);
            if (req.get("username") == null || req.get("password") == null || req.get("email") == null) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: There was a problem with your register info\" }");
                return;
            }
            User userData = new User(req.get("username").toString(), req.get("password").toString(), req.get("email").toString());


            if (!accountService.creatAccont(userData)) {
                ctx.status(403);
                ctx.result("{\"message\": \"Error: Your username is already taken\"}");
                return;
            }


            AuthData authToken = accountService.authDataGenorator(userData.username());


            var resp = serializer.toJson(authToken);
            ctx.status(200);
            ctx.result(resp);
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e + "\" }\n");
            return;
        }
    }

    public int run(int desiredPort) {
        if(!isRunning){
            isRunning = true;
            server.start(desiredPort);
            Notification_map = new HashMap<>();
        }
        return server.port();
    }

    public void stop() {
        if(isRunning){
            isRunning = false;

            server.stop();
        }
    }
}
