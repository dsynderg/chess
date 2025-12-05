package client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import services.PlayService;
import services.WsEchoClient;
import services.WsHelper;
import websocket.commands.UserGameCommand;

import java.net.URISyntaxException;
import java.util.Scanner;

import static services.PlayService.makeMoveHelper;

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
        port = server.run(1000);
        System.out.println("Started test HTTP server on " + port);
//        client = new WsEchoClient(0);
    }


    @AfterAll
    public static void close() {
        server.stop();
    }

    @Test
    void multiplewebsocketConnection() throws Exception {
        var newobserver = new WsHelper(port, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "564", "bob", 123), true);
        Thread.sleep(500);

        var extraobserver = new WsHelper(port, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "4569", "don", 123), true);
        Thread.sleep(500);
        assert newobserver.hasRecivedMessage;
        Thread.sleep(500);
        assert extraobserver.hasRecivedMessage;
        var newobserverdiffgame = new WsHelper(port, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "sldfkj", "fill", 789), true);
        assert extraobserver.hasRecivedMessage;
    }

    @Test
    public void checkmakemoveHelper() {
        String startPosition = "A1";
        var positionPair = makeMoveHelper(startPosition);
        assert positionPair != null;
    }

    

    @Test
    public void loadGame() throws Exception {
//        var
    }


}
