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
        var newobserver = new WsHelper(8080, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "564", "bob", 123), true);
        Thread.sleep(500);

        var extraobserver = new WsHelper(8080, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "4569", "don", 123), true);
        Thread.sleep(500);
        assert newobserver.hasRecivedMessage;
        assert !extraobserver.hasRecivedMessage;
        var newobserverdiffgame = new WsHelper(8080, new UserGameCommand(UserGameCommand.CommandType.CONNECT, "sldfkj", "fill", 789), true);
        assert !extraobserver.hasRecivedMessage;
    }

    @Test
    public void checkmakemoveHelper() {
        String startPosition = "A1";
        var positionPair = makeMoveHelper(startPosition);
        assert positionPair != null;
    }

    @Test
    public void echoTest() throws Exception {
        var echoclient = new WsEchoClient(8080);
    }

    @Test
    public void loadGame() throws Exception {
//        var
    }

    @Test
    public void getMoveTest() {
        System.out.println("What move do you want to make (start) (end) (pawn promotion)?");
        String moveString = "a1 g8 knight";
        String[] moveParts = moveString.split(" ");
        String startingPosition = moveParts[0];
        String endPosition = moveParts[1];
        String promoteString = null;
        ChessPiece.PieceType promotionPeice = null;
        var movePair = makeMoveHelper(startingPosition);
        if ((0 > movePair[0] || movePair[0] > 9) || (0 > movePair[1] || movePair[1] > 9)) {

            return;

        }
        var endPair = makeMoveHelper(endPosition);
        if ((0 > endPair[0] || endPair[0] > 9) || (0 > endPair[1] || endPair[1] > 9)) {

            return;
        }
        if (moveParts.length == 3) {
            promoteString = moveParts[2];
            switch (promoteString) {
                case "queen":
                    promotionPeice = ChessPiece.PieceType.QUEEN;
                    break;
                case "rook":
                    promotionPeice = ChessPiece.PieceType.ROOK;
                    break;
                case "bishop":
                    promotionPeice = ChessPiece.PieceType.BISHOP;
                    break;
                case "knight":
                    promotionPeice = ChessPiece.PieceType.KNIGHT;
                    break;

            }
        }
        assert promotionPeice == ChessPiece.PieceType.KNIGHT;
        assert movePair[0] == 1;
        assert movePair[1] == 1;
        assert endPair[0]== 7;
        assert endPair[1] == 8;
    }
}
