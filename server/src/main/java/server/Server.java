package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLTableControler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import modules.AuthData;
import modules.GameData;
import modules.User;
import services.AccountService;
import services.DeleteService;
import services.GameService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server {


    private final Javalin server;
    AccountService accountService = new AccountService();
    GameService gameService = new GameService();

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
        // Register your endpoints and exception handlers here.

    }

    private void joinGame(Context ctx) {
        var serializer = new Gson();
        //validates auth
        try {
            if (!validateAuth(ctx)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: unauthorized\" }");
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
            String joinstring = req.get("playerColor").toString();
            if (Objects.equals(joinstring, "WHITE")) {
                joinColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(joinstring, "BLACK")) {
                joinColor = ChessGame.TeamColor.BLACK;
            } else {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: playerColor was wrong\" }");
                return;
            }
            int gameID = ((Double) req.get("gameID")).intValue();
            if (!gameService.checkGameID(gameID)) {
                ctx.status(400);
                ctx.result("{ \"message\": \"gameID was invalid\" }");
                return;
            }
            if (!gameService.assignColor(username, joinColor, gameID)) {
                ctx.status(403);
                ctx.result("{ \"message\": \"Error: already taken\" }");
                return;
            }
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.toString() + "\" }\n");
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
            ctx.result("{ \"message\": \"Error: bad request\" }");
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
                ctx.result("{ \"message\": \"That game name is already taken\" }");
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
                ctx.result("{ \"message\": \"Error: bad request\" }");
                return;
            }
            User userData = new User(req.get("username").toString(), req.get("password").toString(), "");

            if (!accountService.checkUsername(userData.username())) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: bad request\" }");
                return;
            }


            if (!accountService.checkPassword(userData.password(), userData)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: wrongPassword\" }");
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
                ctx.result("{ \"message\": \"Error: bad request\" }");
                return;
            }
            User userData = new User(req.get("username").toString(), req.get("password").toString(), req.get("email").toString());


            if (!accountService.creatAccont(userData)) {
                ctx.status(403);
                ctx.result("{\"message\": \"Error: already taken\"}");
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
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
