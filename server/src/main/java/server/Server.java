package server;

import Services.AccountService;
import Services.DeleteService;
import Services.GameService;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import modules.AuthData;
import modules.GameData;
import modules.User;

import java.util.Map;

public class Server {

    private final Javalin server;
    AccountService accountService = new AccountService();
    GameService gameService = new GameService();

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db",this::deleteall);
        server.post("user",this::register);
        server.post("session",this::login);
        server.delete("session",this::logout);
        server.get("game",this::listGames);
        server.post("game",this::createGame);
//        server.put("game",this::joinGame);
        // Register your endpoints and exception handlers here.

    }
    private void deleteall(Context ctx){
        var serializer = new Gson();
        boolean deleteSuccess = DeleteService.deleteAll();
        if(!deleteSuccess){
            ctx.status(500);
            ctx.result(serializer.toJson("{ \"message\": \"Error: There was a problem deleting the database\" }"));
            return;
        }
        ctx.status(200);
        ctx.result("{}");
    }
    private boolean validateAuth(Context ctx){
        var authToken = ctx.header("authorization");

        if(accountService.checkAuth(authToken)){
        return true;}
        return false;
    }
    private void createGame(Context ctx){
        if (!validateAuth(ctx)){
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
        }
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);

        GameData gameObject = gameService.gameDataGenorator((String) req.get("gameName"));

    }
    private void listGames(Context ctx){
        var serializer = new Gson();
        if (validateAuth(ctx)==false){
            ctx.result("[401] { \\\"message\\\": \\\"Error: unauthorized\\\" }");
        }
//        ctx.result("[200] { \"games\": [{\"gameID\": 1234, \"whiteUsername\":\"\", \"blackUsername\":\"\", \"gameName:\"\"} ]}");
        Context result = ctx.result("{\"games\":[]}");
    }

    private void logout(Context ctx){
        var serializer = new Gson();
        var authToken = ctx.header("authorization");
        if(!accountService.checkAuth(authToken)){
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }\n");
            return;
        }
        accountService.removeAuth(authToken);
        if(accountService.checkAuth(authToken)){
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: (description of error)\" }\n");
            return;
        }
        // see if the auth token is valid
        ctx.status(200);
        ctx.result("{}");
    }
    private void login(Context ctx){
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        // if either the username or password is null
        if(req.get("username")==null || req.get("password")==null){
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        User userData = new User(req.get("username").toString(),req.get("password").toString(),"");
        if(!accountService.checkUsername(userData.username())){
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        if(!accountService.checkPassword(userData.password(),userData)){
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: wrongPassword\" }");
            return;
        }
        AuthData authData = accountService.authDataGenorator(userData.username());

        var resp = serializer.toJson(authData);
        ctx.status(200);
        ctx.result(resp);
    }
    private void register(Context ctx){
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        if(req.get("username")==null || req.get("password")==null||req.get("email")==null){
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
            return;
        }
        User userData = new User(req.get("username").toString(),req.get("password").toString(),req.get("email").toString());

        if(!accountService.creatAccont(userData)){
            ctx.status(403);
            ctx.result("{\"message\": \"Error: already taken\"}");
            return;
        }
        AuthData authToken = accountService.authDataGenorator(userData.username());


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
