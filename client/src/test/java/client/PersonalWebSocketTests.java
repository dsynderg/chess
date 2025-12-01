package client;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import services.WsEchoClient;
import services.WsHelper;
import websocket.commands.UserGameCommand;

import java.net.URISyntaxException;

public class PersonalWebSocketTests {
    static Server server;
    public static Session session;
    private static int port;
//    static WsEchoClient client;
//    URI uri = new URI("ws://localhost:0/ws");

    public PersonalWebSocketTests() throws URISyntaxException {
    }

    @BeforeAll

    public static void init() throws Exception {
        server = new Server();
        port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
//        client = new WsEchoClient(0);
    }



    @AfterAll
    public static void close(){server.stop();}
    @Test
    void multiplewebsocketConnection() throws Exception {
        var newobserver = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"564","bob",123),true);
        Thread.sleep(500);

        var extraobserver = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"4569","don",123),true);
        Thread.sleep(500);
        assert newobserver.hasRecivedMessage;
        assert !extraobserver.hasRecivedMessage;
        var newobserverdiffgame = new WsHelper(8080,new UserGameCommand(UserGameCommand.CommandType.CONNECT,"sldfkj","fill",789),true);
        assert !extraobserver.hasRecivedMessage;
    }
    @Test
    public void echoTest() throws Exception {
        var echoclient = new WsEchoClient(8080);
    }
    @Test
    public void loadGame() throws Exception {
//        var
    }

}
