package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin server;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db",ctx -> ctx.result("{}"));
        server.post("user",this::register);
        server.post("session",this::login);
        server.delete("session",this::logout);
        server.get("game",this::listGames);
        server.post("game",this::createGame);
//        server.put("game",this::joinGame);
        // Register your endpoints and exception handlers here.

    }
    private boolean validateAuth(Context ctx){

        return true;
    }
    private void createGame(Context ctx){
        if (validateAuth(ctx)==false){
            ctx.result("[401] { \\\"message\\\": \\\"Error: unauthorized\\\" }");
        }
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);

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
        // see if the auth token is valid

        ctx.result("{}");
    }
    private void login(Context ctx){
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        req.put("authToken","cow");
        var resp = serializer.toJson(req);
        ctx.result(resp);
    }
    private void register(Context ctx){
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), Map.class);
        req.put("authToken","cow");
        var resp = serializer.toJson(req);

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
