import chess.ChessGame;
import chess.ChessPiece;
import clientenums.State;
import server.Server;
import java.util.*;

public class Main {
    private State state = State.Loggedout;
    private Server server;

    static void main(String[] args) {
        boolean isLoggedin = false;
        Scanner scanner = new Scanner(System.in);
        LogoutService Logout = new LogoutService();

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("Type HELP to get relevant commands");

        while(true){
            // User user = isLoggedout(); //this is what it will be once isLoggedout();
            Logout.isLoggedout();
            // isLoggedin(user);
        }

    }

}