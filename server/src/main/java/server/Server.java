package server;

import chess.ChessGame;
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
        server.ws("game/{gameID}/{playername}",this::connectGame);
        // Register your endpoints and exception handlers here.

    }
    private void connectGame(WsConfig ws){
        ws.onConnect(ctx -> {
            String gameID = ctx.pathParam("gameID");
            System.out.println("a connection was made to " +gameID);
            Notification_map.computeIfAbsent(gameID, k -> new ArrayList<>());

            ctx.enableAutomaticPings();
            System.out.println("Websocket connected");
            String playerName = ctx.pathParam("playername");
            for(var context:Notification_map.get(gameID)){
                System.out.println(context);
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,"{\"notification\":\""+playerName+" connected\"}");
                String sendingMessage = new Gson().toJson(serverMessage);
                context.send(sendingMessage);
            }
            Notification_map.get(gameID).add(ctx);
        });
        ws.onMessage(ctx -> {
            UserGameCommand command;
            Gson gson = new Gson();
            String message = ctx.message();
            System.out.println(message);
            var ctxMap = gson.fromJson(message,Map.class);

            if (ctxMap.get("commandType")!= UserGameCommand.CommandType.MAKE_MOVE){
                 command = gson.fromJson(ctx.message(), UserGameCommand.class);
            }
            else{
                //this is if the command type is make move. Its different because it
                //implements a differnt class
                 command = gson.fromJson(ctx.message(), MakeMoveCommand.class);
                if(!accountService.checkAuth(command.getAuthToken())){
                    System.out.println(ctx);
                    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You aren't authorized to make this connection connected\"}");
                    String sendingMessage = new Gson().toJson(serverMessage);
                    ctx.send(sendingMessage);
                }
                 // do the make move logic
            }
            if(!accountService.checkAuth(command.getAuthToken())){
                System.out.println(ctx);
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You aren't authorized to make this connection connected\"}");
                String sendingMessage = new Gson().toJson(serverMessage);
                ctx.send(sendingMessage);
            }
            else if(command.getCommandType()== UserGameCommand.CommandType.LEAVE){
                GameData gameData = gameService.inDatabaseID(command.getGameID());
                ServerMessage serverMessage;
                if (Objects.equals(gameData.whiteUsername(), command.getUsername())){
                    gameService.assignColor(null, ChessGame.TeamColor.WHITE,command.getGameID());
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,"{\"notificaiton\":\""+command.getUsername()+" left the game\"}");
                }

                if (Objects.equals(gameData.blackUsername(), command.getUsername())) {
                    gameService.assignColor(null, ChessGame.TeamColor.BLACK, command.getGameID());
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,"{\"notificaiton\":\""+command.getUsername()+" left the game\"}");
                }
                else{
                    serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR,"{\"error\":\"You aren't authorized to make this connection connected\"}");
                }
                for(var context:Notification_map.get(command.getGameID())){
                    System.out.println(context);
                    String sendingMessage = new Gson().toJson(serverMessage);
                    context.send(sendingMessage);
                }


            }
            else if (command.getCommandType()== UserGameCommand.CommandType.RESIGN){
                var games = gameService.getGames();
                for(var game:games){
                    ChessGame.TeamColor winningColor;
                    if (game.whiteUsername()==command.getUsername()){
                       winningColor = ChessGame.TeamColor.BLACK;
                    }
                    else {
                        winningColor = ChessGame.TeamColor.WHITE;
                    }
                    if (game.gameID() == command.getGameID()){
                        gson=new Gson();
                        ChessGame chessGame = game.game();
                        chessGame.winSetter(winningColor);
                        GameData updatedBoard = new GameData(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName(),chessGame);
                        gameService.updateBoard(updatedBoard);

                    }
                }

            }
            else if (command.getCommandType() == UserGameCommand.CommandType.LOAD_GAME){
                var games = gameService.getGames();
                for(var game:games){
                    if(game.gameID()==command.getGameID()){
                        gson = new Gson();
                        String gameJson = gson.toJson(game);
                        ServerMessage returnmessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,gameJson);
                        ctx.send(gson.toJson(returnmessage));
                    }
                }
            }


        });
        ws.onClose(ctx -> {System.out.println("Websocket closed");
            Notification_map.get(ctx.pathParam("gameID")).removeIf(c-> c.sessionId().equals(ctx.sessionId()));
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
