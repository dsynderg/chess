package client;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import services.HttpHelper;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ServerFacadeTests {

    private static Server server;
    private static HttpClient client;
    private static HttpHelper helper;
    private static Gson gson;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(1);
        helper = new HttpHelper();
        helper.port = 1;
        System.out.println("Started test HTTP server on " + port);
        client = HttpClient.newHttpClient();
        gson = new Gson();

    }
    @AfterAll
    static void stopServer() {
        server.stop();

    }

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 12; // change as needed

        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    private Map<String, String> registerJSON() {
        Map<String, String> registerJSONMap = new HashMap<>();
        registerJSONMap.put("username", generateRandomString());
        registerJSONMap.put("password", generateRandomString());
        registerJSONMap.put("email", generateRandomString());
        return registerJSONMap;

    }

    private Map<String,String> loginJSON(String username, String password) {

        Map<String, String> loginJSONMap = new HashMap<>();
        loginJSONMap.put("username", username);
        loginJSONMap.put("password", password);
        return loginJSONMap;
    }

    private  Map<String,String> createGameJSON() {
        Map<String, String> registerJSONMap = new HashMap<>();
        registerJSONMap.put("gameName", generateRandomString());
        return registerJSONMap;
    }

        private Map<String,Object> joinGameJSON(String color, int gameID){
            Map<String, Object> joinGameJSONMap = new HashMap<>();
            joinGameJSONMap.put("playerColor", color);
            joinGameJSONMap.put("gameID", gameID);
            return joinGameJSONMap;

    }


    private AuthData registerForAuth() throws Exception {
        var json = registerJSON();
        HttpResponse<String> data = helper.requestMaker(RequestType.post,"user",gson.toJson(json),null);
        assert data.statusCode() == 200;
        return gson.fromJson(data.body(),AuthData.class);

    }
    @Test
    public void register() throws Exception {

        var json = registerJSON();
        HttpResponse<String> data = helper.requestMaker(RequestType.post,"user",gson.toJson(json),null);
        assert data.statusCode() == 200;
    }
    @Test
    public void logout() throws Exception {
        AuthData data = registerForAuth();
        var resp = helper.requestMaker(RequestType.delete,"session",null,data);
        assert resp.statusCode() == 200;
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
