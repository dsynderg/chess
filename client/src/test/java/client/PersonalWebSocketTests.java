package client;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import services.WsEchoClient;

import java.net.URI;
import java.net.URISyntaxException;

public class PersonalWebSocketTests {
    static Server server;
    public static Session session;
    static WsEchoClient client;
    URI uri = new URI("ws://localhost:0/ws");

    public PersonalWebSocketTests() throws URISyntaxException {
    }

    @BeforeAll

    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        client = new WsEchoClient(0);
    }
    @AfterAll
    public static void close(){server.stop();}
    @Test
    public void echoTest() {}

}
