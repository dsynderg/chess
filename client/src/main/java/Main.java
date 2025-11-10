import chess.ChessGame;
import chess.ChessPiece;
import clientenums.State;
import modules.AuthData;
import modules.User;
import server.Server;
import services.LogoutService;

import java.util.*;

public class Main {
    private State state = State.Loggedout;
    private static AuthData authData;
    private static User user;


    static void main(String[] args) {
        boolean isLoggedin = false;
        Scanner scanner = new Scanner(System.in);
        LogoutService Logout = new LogoutService();

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("Type HELP to get relevant commands");

        while(true){
            // User user = isLoggedout(); //this is what it will be once isLoggedout();
            var authAndUser = Logout.isLoggedout();
            authData = authAndUser.getKey();
            user = authAndUser.getValue();
        }

    }

}