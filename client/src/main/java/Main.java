import chess.ChessGame;
import chess.ChessPiece;
import modules.AuthData;
import modules.User;
import services.LoginService;
import services.LogoutService;

import java.util.*;

public class Main {
    private static AuthData authData;
    private static User user;


    static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        LogoutService logout = new LogoutService();
        LoginService login = new LoginService();

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("Type HELP to get relevant commands");

        while(true){
            var authAndUser = logout.isLoggedout();
            assert authAndUser != null;
            authData = authAndUser.getKey();
            //fix this so we are catching and handleing the exception instead of just throwing it
            login.isLoggedin(authData);
            authData=null;
        }

    }

}