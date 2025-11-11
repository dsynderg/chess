package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import server.Server;

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

    public HttpResponse<String> requestMaker(RequestType type, String path, String json, AuthData data) throws Exception {

//        server.run(port);
        try (HttpClient client = HttpClient.newHttpClient()) {
            if (json == null) {
                json = "{}";
            }
            String fullpath = "http://localhost:"+port+"/" + path;
            if (type == RequestType.get) {
                if(Objects.equals(path,"game")){
                    Gson gson = new Gson();
                    var response = send(client, HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .GET()
                            .header("authorization", data.authToken())
                            .build());
                    System.out.println("games were listed");
                    var responsemap = gson.fromJson(response.body(), Map.class);
                    var responselist =  responsemap.get("games");
//                    System.out.println(responselist);
                    //
                    for(var game: (Iterable<?>) responselist) System.out.println(game);
                }

            }
            if (type == RequestType.put) {
                if(Objects.equals(path, "game")){
                    return send(client,HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .header("authorization", data.authToken())
                            .build());
                }
            }
            if (type == RequestType.delete) {
                if (Objects.equals(path,"session")){
                    return send(client,HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .DELETE()
                            .header("authorization",data.authToken())
                            .build());
                }

            }
            if (type == RequestType.post) {
                if (Objects.equals(path, "session")){
                    return send(client, HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build());
                }

                if(Objects.equals(path, "user")){
                    return send(client, HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build());
                }
                if(Objects.equals(path,"game")){
                    return send(client, HttpRequest.newBuilder()
                            .uri(new URI(fullpath))
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .header("authorization", data.authToken())
                            .build());
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



