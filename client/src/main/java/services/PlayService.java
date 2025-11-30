package services;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import java.util.Objects;
import java.util.Scanner;

public class PlayService {
    private static ChessBoard board;
    private static WsHelper helper;
    private static Gson gson;
    private static  void drawboard(ChessBoard board){
        BoardPrinter.printBoard(board, ChessGame.TeamColor.WHITE);
    }
    private static  void help(Boolean isPlayer){
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("DRAWBOARD: Redraws the board");
        System.out.println("LEAVE: Step away from the chess board");
        if(isPlayer) {
            System.out.println("RESIGN: You automaticly loose");
            System.out.println("MAKEMOVE: You make a chess move");
            System.out.println("HIGHLIGHT: You give a piece on the board and it shows you the possible moves");
        }
    }
    private static void leave(){}
    private static void resign(){}
    private static void makeMove(ChessMove move){}
    private static void hilightLegalMoves(ChessPosition position){}
    public static void playRepl(UserGameCommand command, Boolean isPlayer) throws Exception {

        //if isPlayer is false then he is an observer
        if(!isPlayer){
            helper = new WsHelper(8080,command,false);
        }
        else if(isPlayer){
            helper = new WsHelper(8080,command,true);

        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("PLAY GAME>>> ");
            String line = scanner.nextLine().trim().toLowerCase();
            if (Objects.equals(line, "help")) {
                help(isPlayer);
            }
            if (Objects.equals(line, "leave")) {
                //do the proper back end to remove the player from the chess game object
                helper.close();
                return;
            }
            if (Objects.equals(line, "drawboard")) {
                UserGameCommand drawCommand = new UserGameCommand(UserGameCommand.CommandType.LOAD_GAME,
                        command.getAuthToken(),
                        command.getUsername(),
                        command.getGameID());
                gson = new Gson();
                var drawcommandjson = gson.toJson(drawCommand);
                helper.send(gson.toJson(drawCommand));
                drawboard(new ChessBoard());
            }
            if(isPlayer) {
                if (Objects.equals(line, "resign")) {
                    resign();
                }

                if (Objects.equals(line, "makemove")) {
                    //eventually you need to add a way for the user to give their own move
                    makeMove(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 1), null));
                }
                if (Objects.equals(line, "highlight")) {
                    //get the chess position from the player
                    hilightLegalMoves(new ChessPosition(1, 1));

                }
            }

        }
    }

}
