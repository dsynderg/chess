package services;

import clientenums.RequestType;
import modules.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import server.Server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                var response = send(client, HttpRequest.newBuilder()
                        .uri(new URI(fullpath))
                        .GET()
                        .header("Authorization", "secret1")
                        .build());
            }
            if (type == RequestType.put) {
            }
            if (type == RequestType.delete) {

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
            }
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        try (HttpClient client = HttpClient.newHttpClient()) {


            send(client, HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/name/joe"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Authorization", "secret1")
                    .build());

            send(client, HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/name"))
                    .PUT(HttpRequest.BodyPublishers.ofString("{\"joe\":\"sue\"}"))
                    .header("Authorization", "secret1")
                    .build());

            send(client, HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/name"))
                    .GET()
                    .build());
        }
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



