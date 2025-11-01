package server;

import dataaccess.DataAccessException;
import services.AccountService;
import services.DeleteService;
import services.GameService;
import chess.ChessGame;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import modules.AuthData;
import modules.GameData;
import modules.User;

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
        server = Javalin.create(config -> config.staticFiles.add("web"));
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
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e.toString()+"\" }\n");
            return;
        }
        String username;
        try {
            username = accountService.getUsernameFromAuth(ctx.header("authorization"));
        } catch (SQLException | DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        //makes sure the body is good

        var req = serializer.fromJson(ctx.body(), Map.class);
        if (req.get("playerColor") == null || req.get("gameID") == null) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        //makes sure that the game id is valid

        //checks joincolor
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
        try {
            if (!gameService.checkGameID(gameID)) {
                ctx.status(400);
                ctx.result("{ \"message\": \"gameID was invalid\" }");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        try {
            if (!gameService.assignColor(username, joinColor, gameID)) {
                ctx.status(403);
                ctx.result("{ \"message\": \"Error: already taken\" }");
                return;
            }
        } catch (SQLException | DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        ctx.status(200);
        ctx.result("{}");
    }

    private void deleteall(Context ctx) {
        var serializer = new Gson();
        boolean deleteSuccess;
        try {
            deleteSuccess = DeleteService.deleteAll();
        } catch (SQLException | DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        if (!deleteSuccess) {
            ctx.status(500);
            ctx.result(serializer.toJson("{ \"message\": \"Error: There was a problem deleting the database\" }"));
            return;
        }
        ctx.status(200);
        ctx.result("{}");
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
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }

        GameData gameObject;
        try {
            gameObject = gameService.gameDataGenorator((String) req.get("gameName"));
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: There was a database error\" }\n");
            return;
        }
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

    }

    private void listGames(Context ctx) {
        var serializer = new Gson();
        try {
            if (!validateAuth(ctx)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: unauthorized\" }");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: There was a database error\" }\n");
            return;
        }
//        ctx.result("[200] { \"games\": [{\"gameID\": 1234, \"whiteUsername\":\"\", \"blackUsername\":\"\", \"gameName:\"\"} ]}");
        ArrayList<GameData> gameList;
        try {
            gameList = gameService.getGames();
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("games", gameList);
        ctx.status(200);
        System.out.println(serializer.toJson(response));
        ctx.result(serializer.toJson(response));
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
        } catch (DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        try {
            accountService.removeAuth(authToken);
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        try {
            if (accountService.checkAuth(authToken)) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: (description of error)\" }\n");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        // see if the auth token is valid
        ctx.status(200);
        ctx.result("{}");
    }

    private void login(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        // if either the username or password is null
        if (req.get("username") == null || req.get("password") == null) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        User userData = new User(req.get("username").toString(), req.get("password").toString(), "");
        try {
            if (!accountService.checkUsername(userData.username())) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: bad request\" }");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        try {
            if (!accountService.checkPassword(userData.password(), userData)) {
                ctx.status(401);
                ctx.result("{ \"message\": \"Error: wrongPassword\" }");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        AuthData authData = null;
        try {
            authData = accountService.authDataGenorator(userData.username());
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }

        var resp = serializer.toJson(authData);
        ctx.status(200);
        ctx.result(resp);
    }

    private void register(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        if (req.get("username") == null || req.get("password") == null || req.get("email") == null) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        User userData = new User(req.get("username").toString(), req.get("password").toString(), req.get("email").toString());

        try {
            if (!accountService.creatAccont(userData)) {
                ctx.status(403);
                ctx.result("{\"message\": \"Error: already taken\"}");
                return;
            }
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: "+e+"\" }\n");
            return;
        }
        AuthData authToken = null;
        try {
            authToken = accountService.authDataGenorator(userData.username());
        } catch ( DataAccessException e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: There was a database error\" }\n");
            return;
        }


        var resp = serializer.toJson(authToken);
        ctx.status(200);
        ctx.result(resp);
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
