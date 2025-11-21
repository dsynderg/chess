package services;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import modules.AuthData;

import java.util.Objects;
import java.util.Scanner;

public class PlayService {

    private static  void drawboard(ChessBoard board){
        BoardPrinter.printBoard(board, ChessGame.TeamColor.WHITE);
    }
    private static  void help(){
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("DRAWBOARD: Logs you out");
        System.out.println("LEAVE: Creates a new game");
        System.out.println("RESIGN: Prints out all of the joinable games");
        System.out.println("MAKEMOVE: joins a game");
        System.out.println("HIGHLIGHT: allows you to oberve a game");
    }
    private static void leave(){}
    private static void resign(){}
    private static void makeMove(ChessMove move){}
    private static void hilightLegalMoves(ChessPosition position){}
    public static void playRepl(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("LOGGED_OUT>>>");
            String line = scanner.nextLine().trim().toLowerCase();
            if (Objects.equals(line, "help")) {
                help();
            }
            if (Objects.equals(line, "leave")) {
                leave();
            }
            if (Objects.equals(line, "resign")) {
                resign();
            }
            if (Objects.equals(line, "makemove")) {
                //eventually you need to add a way for the user to give their own move
                makeMove(new ChessMove(new ChessPosition(1,1),new ChessPosition(1,1),null));
            }
            if (Objects.equals(line, "highlight")) {
                //get the chess position from the player
                hilightLegalMoves(new ChessPosition(1,1));

            }
            if (Objects.equals(line, "drawboard")) {
                drawboard(new ChessBoard());
            }
        }
    }

}
