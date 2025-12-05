package services;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import modules.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.*;

public class WebsocketService {
    GameService gameService = new GameService();
    AccountService accountService = new AccountService();
    Gson gson = new Gson();

    Map<String, HashSet<WsContext>> notificationMap = new HashMap<>();

    private void notificationSender(String gameID, ServerMessage serverMessage) {

        Gson gson = new Gson();
        var sessions = new HashSet<>(notificationMap.get(gameID));
        for (var context : sessions) {
            String sendingMessage = gson.toJson(serverMessage);
            if (context.session.isOpen()) {
                context.send(sendingMessage);
            } else {
                var newsessions = notificationMap.get(gameID);
                newsessions.remove(context);
                notificationMap.put(gameID, newsessions);

            }
        }
    }

    private void notificationSenderminusOne(String gameID, ServerMessage message, WsContext ctx) {
        Gson gson = new Gson();
        var sessions = new HashSet<>(notificationMap.get(gameID));

        for (var context : sessions) {
            System.out.println(context);
            if (!context.equals(ctx)) {
                String sendingMessage = gson.toJson(message);
                if (context.session.isOpen()) {
                    context.send(sendingMessage);
                } else {
                    var newsessions = notificationMap.get(gameID);
                    newsessions.remove(context);
                    notificationMap.put(gameID, newsessions);

                }
            }
        }
    }

    public void makeMove(WsMessageContext ctx) throws DataAccessException {
        Gson gson = new Gson();
        var moveCommand = gson.fromJson(ctx.message(), MakeMoveCommand.class);

        try {
            if (!accountService.checkAuth(moveCommand.getAuthToken())) {
                System.out.println(ctx);

                throw new Exception();
            }
        } catch (Exception e) {
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
        try {
            String username = accountService.getUsernameFromAuth(moveCommand.getAuthToken());
            var playersColor = (Objects.equals(username, gameData.whiteUsername())) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            var board = chessGame.getBoard();
            if (playersColor != board.getColor(move.getStartPosition())) {
                throw new InvalidMoveException("");
            }
            chessGame.makeMove(move);
            GameData newGameData = new GameData(gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    chessGame);
            var games = gameService.getGames();

            for (var game : games) {
                if (game.gameID() == moveCommand.getGameID()) {
                    String moversUsername = accountService.getUsernameFromAuth(moveCommand.getAuthToken());
                    if (!Objects.equals(moversUsername, game.whiteUsername()) && !Objects.equals(moversUsername, game.blackUsername())) {
                        throw new InvalidMoveException("");
                    }

                    gson = new Gson();
                    String gameJson = gson.toJson(game);
                    String playerColor = (Objects.equals(moveCommand.getUsername(), newGameData.whiteUsername())) ? "White" : "Black";
                    gameService.updateBoard(newGameData);
                    String updatedGameJson = gson.toJson(newGameData);
                    Thread.sleep(300);
                    LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGameJson);
                    notificationSender(String.valueOf(game.gameID()), loadGameMessage);
                    NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "{\"notification\":\""+playerColor+ " has made a move\"}");
                    notificationSenderminusOne(String.valueOf(game.gameID()), notificationMessage, ctx);
                }
            }
        } catch (InvalidMoveException e) {
            System.out.println("that was a bad move G");
            //{"error":"The move that you tried to make was invalid"}
            ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "{\"error\":\"The move that you tried to make was invalid\"}");
            ctx.send(gson.toJson(errorMessage));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // do the make move logic
    }

    public void leave(WsMessageContext ctx) throws DataAccessException {
        var command = gson.fromJson(ctx.message(), UserGameCommand.class);

        GameData gameData = gameService.inDatabaseID(command.getGameID());
        NotificationMessage serverMessage;
        var username = accountService.getUsernameFromAuth(command.getAuthToken());
        if (Objects.equals(gameData.whiteUsername(), username)) {
            gameService.assignColor(null, ChessGame.TeamColor.WHITE, command.getGameID());
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            gameService.assignColor(null, ChessGame.TeamColor.BLACK, command.getGameID());
        }

        serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                "{\"notification\":\"" + command.getUsername() + " left the game\"}");
        notificationMap.get(String.valueOf(command.getGameID())).removeIf(c -> c.sessionId().equals(ctx.sessionId()));
        notificationSender(String.valueOf(command.getGameID()), serverMessage);
    }

    public void resign(WsMessageContext ctx) throws DataAccessException {
        var command = gson.fromJson(ctx.message(), UserGameCommand.class);
        var games = gameService.getGames();
        ChessGame.TeamColor winningColor = null;
        for (var game : games) {

            if (game.gameID() == command.getGameID()) {
                if (game.game().hasWon() != null) {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "{\"error\":\"You cant resign the gam\"}");
                    ctx.send(gson.toJson(errorMessage));
                }
                var username = accountService.getUsernameFromAuth(command.getAuthToken());
                if (Objects.equals(game.whiteUsername(), username)) {
                    winningColor = ChessGame.TeamColor.BLACK;
                } else if (Objects.equals(game.blackUsername(), username)) {
                    winningColor = ChessGame.TeamColor.WHITE;
                } else {
                    ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "{\"error\":\"You arnt authorized to resign\"}");
                    ctx.send(gson.toJson(errorMessage));
                }
                if (game.gameID() == command.getGameID()) {
                    ChessGame chessGame = game.game();
                    chessGame.winSetter(winningColor);
                    GameData updatedBoard = new GameData(game.gameID(),
                            game.whiteUsername(),
                            game.blackUsername(),
                            game.gameName(),
                            chessGame);
                    gameService.updateBoard(updatedBoard);

                }
            }
        }
        String winnerString = (winningColor == ChessGame.TeamColor.WHITE) ? "White" : "Black";
        ServerMessage declareWinner = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                "{\"notification\":\"" + winnerString + " has won the game!!!!\"}");
//
        notificationSender(String.valueOf(command.getGameID()), declareWinner);
    }

    public void loadGame(WsMessageContext ctx) throws DataAccessException {
        var command = gson.fromJson(ctx.message(), UserGameCommand.class);
        var games = gameService.getGames();
        for (var game : games) {
            if (game.gameID() == command.getGameID()) {
                gson = new Gson();
                String gameJson = gson.toJson(game);
                LoadGameMessage returnmessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
                ctx.send(gson.toJson(returnmessage));
            }
        }
    }

    public void connect(WsMessageContext ctx) throws DataAccessException {
        var command = gson.fromJson(ctx.message(), UserGameCommand.class);
        String gameID = String.valueOf(command.getGameID());
        boolean gameExists = gameService.checkGameID(command.getGameID());
        if (!gameExists) {
            gson = new Gson();
            ErrorMessage returnmessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "{\"error\":\"Your gameID is bad please try again\"}");
            ctx.send(gson.toJson(returnmessage));
        } else {
            System.out.println("a connection was made to " + gameID);
            notificationMap.computeIfAbsent(gameID, k -> new HashSet<>());

            ctx.enableAutomaticPings();
            String playerName = command.getUsername();


            var games = gameService.getGames();
            boolean gameIDfound = false;
            for (var game : games) {
                if (game.gameID() == command.getGameID()) {
                    String playerPosition = (Objects.equals(command.getUsername(), game.whiteUsername())) ? "white": (Objects.equals(command.getUsername(),game.blackUsername())) ? "black": "observer";
                    NotificationMessage serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                            "{\"notification\":\"" + playerName + " connected as "+playerPosition+"\"}");
                    notificationSender(gameID, serverMessage);
                    notificationMap.get(gameID).add(ctx);
                    gson = new Gson();
                    String gameJson = gson.toJson(game);
                    LoadGameMessage returnmessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
                    ctx.send(gson.toJson(returnmessage));
                    System.out.println(returnmessage);
                }
            }
        }
    }
}


