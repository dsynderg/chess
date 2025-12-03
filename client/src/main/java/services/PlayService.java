package services;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class PlayService {
    private static ChessBoard board;
    private static WsHelper helper;
    private static Gson gson;
    private static  void drawboard(UserGameCommand command, WsHelper helper ) throws IOException {
        UserGameCommand drawCommand = new UserGameCommand(UserGameCommand.CommandType.LOAD_GAME,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID());
        gson = new Gson();
        var drawcommandjson = gson.toJson(drawCommand);
        helper.send(drawcommandjson);
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

    private static void leave(boolean isPlayer, UserGameCommand command, WsHelper helper) throws IOException {
        if(isPlayer) {
            UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                    command.getAuthToken(),
                    command.getUsername(),
                    command.getGameID());
            gson = new Gson();
            var leaveJson = gson.toJson(leaveCommand);
            helper.send(leaveJson);
        }
        helper.close();
    }

    private static void resign(UserGameCommand command,WsHelper helper) throws IOException {
        UserGameCommand resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID());
        gson=new Gson();
        String resignJson = gson.toJson(resignCommand);
        helper.send(resignJson);

    }
    private static void makeMove(ChessMove move){}
    private static int columnMakeMoveHelper(char column) {
        column = Character.toLowerCase(column);
        switch (column) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
        }
        return 100;
    }

    private static void hilightLegalMoves(ChessPosition position){}
    public static void playRepl(UserGameCommand command, Boolean isPlayer) throws Exception {
        //if isPlayer is false then he is an observer
        gson = new Gson();
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
                leave(isPlayer,command,helper);
                return;
            }
            if (Objects.equals(line, "drawboard")) {
//                UserGameCommand drawCommand = new UserGameCommand(UserGameCommand.CommandType.LOAD_GAME,
//                        command.getAuthToken(),
//                        command.getUsername(),
//                        command.getGameID());
//                gson = new Gson();
//                var drawcommandjson = gson.toJson(drawCommand);
//                helper.send(drawcommandjson);
                drawboard(command, helper);
//                drawboard(new ChessBoard());
            }
            if(isPlayer) {
                if (Objects.equals(line, "resign")) {
                    resign(command,helper);
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
