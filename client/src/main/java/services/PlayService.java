package services;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.glassfish.grizzly.utils.Pair;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Scanner;

public class PlayService {
    private static ChessBoard board;
    private static WsHelper helper;
    private static Gson gson;

    private static void drawboard(UserGameCommand command, WsHelper helper) throws IOException {
        UserGameCommand drawCommand = new UserGameCommand(UserGameCommand.CommandType.LOAD_GAME,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID());
        gson = new Gson();
        var drawcommandjson = gson.toJson(drawCommand);
        helper.send(drawcommandjson);
    }

    private static void help(Boolean isPlayer) {
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("DRAWBOARD: Redraws the board");
        System.out.println("LEAVE: Step away from the chess board");
        if (isPlayer) {
            System.out.println("RESIGN: You automaticly loose");
            System.out.println("MAKEMOVE: You make a chess move");
            System.out.println("HIGHLIGHT: You give a piece on the board and it shows you the possible moves");
        }
    }

    private static void leave(boolean isPlayer, UserGameCommand command, WsHelper helper) throws IOException {
//        if (isPlayer) {
        UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID());
        gson = new Gson();
        var leaveJson = gson.toJson(leaveCommand);
        helper.send(leaveJson);
//        }
        helper.close();
    }

    private static void resign(UserGameCommand command, WsHelper helper) throws IOException {
        UserGameCommand resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID());
        gson = new Gson();
        String resignJson = gson.toJson(resignCommand);
        helper.send(resignJson);

    }

    private static void makeMove(ChessMove move) {
    }

    private static Pair<Integer, Integer> makeMoveHelper(String move) {
        var moveList = move.chars()
                .mapToObj(c -> (char) c)
                .toList();
        char column = moveList.get(0);
        Character.toLowerCase(column);
        int columnInt;
        switch (column) {
            case 'a':
                columnInt = 1;
            case 'b':
                columnInt = 2;
            case 'c':
                columnInt = 3;
            case 'd':
                columnInt = 4;
            case 'e':
                columnInt = 5;
            case 'f':
                columnInt = 6;
            case 'g':
                columnInt = 7;
            case 'h':
                columnInt = 8;
            default:
                columnInt = 100;

        }
        return new Pair<>(columnInt, Integer.parseInt(String.valueOf(move.charAt(1))));

    }

    private static void hilightLegalMoves(ChessPosition position) {
    }

    public static void playRepl(UserGameCommand command, Boolean isPlayer) throws Exception {
        //if isPlayer is false then he is an observer
        gson = new Gson();
        if (!isPlayer) {
            helper = new WsHelper(8080, command, false);
        } else if (isPlayer) {
            helper = new WsHelper(8080, command, true);

        }
        String connectJson = gson.toJson(command);
        helper.send(connectJson);
//        drawboard(command,helper);
        Scanner scanner = new Scanner(System.in);
        drawboard(command, helper);
        while (!helper.getIsOver()) {
            System.out.print("PLAY GAME>>> ");
            String line = scanner.nextLine().trim().toLowerCase();
            switch (line) {
                case "help" -> help(isPlayer);

                case "leave" -> {
                    // do the proper back end to remove the player from the chess game object
                    leave(isPlayer, command, helper);
                    return;
                }

                case "drawboard" -> {
                    drawboard(command, helper);
                    // drawboard(new ChessBoard());
                }

                case "resign" -> {
                    if (isPlayer) {
                        resign(command, helper);
                        // return;
                    }
                }

                case "makemove" -> {
                    if (isPlayer) {
                        // eventually you need to add a way for the user to give their own move
                        System.out.println("What is the position of the peice you want to move?");
                        makeMove(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 1), null));
                    }
                }

                case "highlight" -> {
                    if (isPlayer) {
                        // get the chess position from the player
                        hilightLegalMoves(new ChessPosition(1, 1));
                    }
                }


            }


        }
        helper.close();
        return;
    }

}
