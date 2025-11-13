package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import modules.GameData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class HttpHelper {


    public static int port = 8080;

    //    private static final Server server = new Server();
    private ArrayList<com.google.gson.internal.LinkedTreeMap> getAllGames(HttpClient client, AuthData data) throws Exception {
        Gson gson = new Gson();
        String fullpath = "http://localhost:" + port + "/game";
        var response = send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .GET()
                .header("authorization", data.authToken())
                .build());
        if (response != null) {
//            System.out.println("If no player is shown in a game, that means that the seat is open");
            var responsemap = gson.fromJson(response.body(), Map.class);
            return (ArrayList<com.google.gson.internal.LinkedTreeMap>) responsemap.get("games");
        }
        return null;
    }

    private void listgames(HttpClient client, AuthData data) throws Exception {
        Gson gson = new Gson();
        ;
        var responselist = getAllGames(client, data);
//                    System.out.println(responselist);
        //
        int counter = 0;
        for (var game : (Iterable<?>) responselist) {
            counter++;
            GameData gamedata = gson.fromJson(game.toString(), GameData.class);
            System.out.println(counter + " Name: "
                    + gamedata.gameName()
                    +" White player: "
                    + gamedata.whiteUsername()
                    + " Black player: "
                    + gamedata.blackUsername());
        }
    }

    private HttpResponse<String> createGame(HttpClient client, 
                                            String fullpath,
                                            AuthData data,
                                            String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("authorization", data.authToken())
                .build());
    }

    private HttpResponse<String> register(HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());
    }

    private HttpResponse<String> login(HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());
    }

    private HttpResponse<String> logout(HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .DELETE()
                .header("authorization", data.authToken())
                .build());

    }

    private HttpResponse<String> joinGame(HttpClient client, String fullpath, AuthData data, String json) throws Exception {
        var responselist = getAllGames(client,data);
        Gson gson = new Gson();
        var jsonmap = gson.fromJson(json,Map.class);
        var gameID = ((Double) jsonmap.get("gameID")).intValue()-1;
        var playerColor = jsonmap.get("playerColor");
        if(!playerColor.equals("white")&&!playerColor.equals("black")) {throw new Exception();}
        if(gameID>responselist.size()) {throw new IndexOutOfBoundsException();}
        var responseobject = (responselist.get(gameID));
        var responsemap = gson.fromJson(String.valueOf(responseobject),GameData.class);
        jsonmap.put("gameID",responsemap.gameID());
        json = gson.toJson(jsonmap);
        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("authorization", data.authToken())
                .build());
    }

    public HttpResponse<String> requestMaker(RequestType type, String path, String json, AuthData data) throws Exception {

//        server.run(port);
        try (HttpClient client = HttpClient.newHttpClient()) {
            if (json == null) {
                json = "{}";
            }
            String fullpath = "http://localhost:" + port + "/" + path;
            if (type == RequestType.get) {
                if (Objects.equals(path, "game")) {
                    listgames(client, data);

                }

            }
            if (type == RequestType.put) {
                if (Objects.equals(path, "game")) {
                    return joinGame(client, fullpath, data, json);

                }
            }
            if (type == RequestType.delete) {
                if (Objects.equals(path, "session")) {
                    return logout(client, fullpath, data, json);

                }

            }
            if (type == RequestType.post) {
                if (Objects.equals(path, "session")) {
                    return login(client, fullpath, data, json);
                }

                if (Objects.equals(path, "user")) {
                    return register(client, fullpath, data, json);
                }
                if (Objects.equals(path, "game")) {
                    return createGame(client, fullpath, data, json);
                }
            }
        }
        return null;
    }


    private static HttpResponse<String> send(HttpClient client, HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        String body = gson.fromJson(response.body(),Map.class).get("message").toString();
        if (response.statusCode() == 200) {
            return response;
        } else if (response.statusCode() == 500) {
            System.out.println("There was a server error");
        }
        else if (response.statusCode() == 401){
            System.out.println("You arn't authorized to make this request");
        }
        else {
        System.out.println(body);
        }
        return null;

    }
}



