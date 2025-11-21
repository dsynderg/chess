package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

public class PersonalWebSocketTests {
    static Server server;
    @BeforeAll

    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }
    @AfterAll
    public static void close(){server.stop();}
    @Test
    public void echoTest() {}
}
