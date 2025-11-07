import chess.ChessGame;
import chess.ChessPiece;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static void help(){
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("LOGIN: Allows you to access a previously regestered accout");
        System.out.println("REGISTER: Creates a new accout");
        System.out.println("QUIT: Ends your session");




    }
    private static void login(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("does login logic");

    }
    private static void register(){
        System.out.println("does register logic");

    }
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("Type HELP to get relevant commands");
        while(true){
            String line = scanner.nextLine().trim().toLowerCase();
            if(Objects.equals(line, "help")){help();}
            if(Objects.equals(line, "quit")){return;}
            if(Objects.equals(line, "login")){login();}
            if (Objects.equals(line, "register")) {register();}

        }
    }
}