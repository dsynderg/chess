package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.EscapeSequences;

public class ClientTest {
    static Server server;
    @Test
    void printWhitePieces() {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK); // 44 = blue background, 37 = white text

        System.out.println("White King: \u2654");
        System.out.println("White Queen: \u2655");
        System.out.println("White Rook: \u2656");
        System.out.println("White Bishop: \u2657");
        System.out.println("White Knight: \u2658");
        System.out.println("White Pawn: \u2659");

    }

    @Test
    void printBlackPieces() {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        System.out.println("Black King: \u265A");
        System.out.println("Black Queen: \u265B");
        System.out.println("Black Rook: \u265C");
        System.out.println("Black Bishop: \u265D");
        System.out.println("Black Knight: \u265E");
        System.out.println("Black Pawn: \u265F");
    }

    @Test
    void clearTheScreen() throws InterruptedException {
        System.out.println("i want to erase everything");
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK); // 44 = blue background, 37 = white text

        System.out.println("Black King: " + EscapeSequences.BLACK_KING);
        System.out.println("Black Queen: \u265B");
        System.out.println("Black Rook: \u265C");
        System.out.println("Black Bishop: \u265D");
        System.out.println("Black Knight: \u265E");
        System.out.println("Black Pawn: \u265F");
        Thread.sleep(1000);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        Thread.sleep(1000);
    }

    @Test
    void backgroundTest() throws InterruptedException {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK); // 44 = blue background, 37 = white text
//        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.print("This has a blue background!");
        Thread.sleep(1000);
        System.out.print(EscapeSequences.ERASE_SCREEN);
        System.out.flush();
//        Thread.sleep(5000);
    }

    @Test
    void chessBoardInTerm() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                System.out.print("   ");
                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                System.out.print("   ");
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
            for (int j = 0; j < 4; j++) {
                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                System.out.print("   ");
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                System.out.print("   ");
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
    }


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
}



