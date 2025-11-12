package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import server.Server;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpHelper {



    public static int port = 8080;
//    private static final Server server = new Server();
    private void listgames(HttpClient client, String fullpath, AuthData data) throws Exception {
        Gson gson = new Gson();
        var response = send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .GET()
                .header("authorization", data.authToken())
                .build());
        if (response != null) {
            System.out.println("If no player is shown in a game, that means that the seat is open");
            var responsemap = gson.fromJson(response.body(), Map.class);
            var responselist = responsemap.get("games");
//                    System.out.println(responselist);
            //
            for (var game : (Iterable<?>) responselist) System.out.println(game);
        }
        else {
            System.out.println("There were no games to be listed");
        }
    }
    private HttpResponse<String> createGame(HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("authorization", data.authToken())
                .build());
    }
    private HttpResponse<String> register (HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());
    }
    private HttpResponse<String> login (HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client, HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());
    }
    private HttpResponse<String> logout (HttpClient client, String fullpath, AuthData data, String json) throws Exception {

        return send(client,HttpRequest.newBuilder()
                .uri(new URI(fullpath))
                .DELETE()
                .header("authorization",data.authToken())
                .build());

    }
    private HttpResponse<String> joinGame (HttpClient client, String fullpath, AuthData data, String json) throws Exception {
        return send(client,HttpRequest.newBuilder()
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
            String fullpath = "http://localhost:"+port+"/" + path;
            if (type == RequestType.get) {
                if(Objects.equals(path,"game")){
                    listgames(client,fullpath,data);

                }

            }
            if (type == RequestType.put) {
                if(Objects.equals(path, "game")){
                    return joinGame(client,fullpath,data,json);

                }
            }
            if (type == RequestType.delete) {
                if (Objects.equals(path,"session")){
                    return logout(client,fullpath,data,json);

                }

            }
            if (type == RequestType.post) {
                if (Objects.equals(path, "session")){
                    return login(client,fullpath,data,json);
                }

                if(Objects.equals(path, "user")){
                    return register(client,fullpath,data,json);
                }
                if(Objects.equals(path,"game")){
                    return createGame(client,fullpath,data,json);
                }
            }
        }
        return null;
    }


    private static HttpResponse<String> send(HttpClient client, HttpRequest request) throws Exception {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200) {
            return response;
        } else {
            System.out.println("Error: received status code " + response.statusCode());
        }
        return null;

    }
//    @AfterAll
//    static void stopServer(){
//        server.stop();
//    }
//    @BeforeAll
//    static void startserver(){
//    }
}



