package services;

import chess.*;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
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

    private static void makeMove(ChessMove move,UserGameCommand command, WsHelper helper) throws IOException {

        MakeMoveCommand moveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                command.getAuthToken(),
                command.getUsername(),
                command.getGameID(),
                move);
        gson = new Gson();
        String moveJson = gson.toJson(moveCommand);
        helper.send(moveJson);
    }

    public static int[] makeMoveHelper(String move) {
        var moveList = move.chars()
                .mapToObj(c -> (char) c)
                .toList();
        char column = moveList.get(0);
        column = Character.toLowerCase(column);
        int columnInt;
        switch (column) {
            case 'a':
                columnInt = 1;
                break;
            case 'b':
                columnInt = 2;
                break;
            case 'c':
                columnInt = 3;

                break;
            case 'd':
                columnInt = 4;

                break;
            case 'e':
                columnInt = 5;

                break;
            case 'f':
                columnInt = 6;

                break;
            case 'g':
                columnInt = 7;

                break;
            case 'h':
                columnInt = 8;

                break;
            default:
                columnInt = 100;

        }
        int[] returnlist = new int[2];
        returnlist[0] = (columnInt);
        returnlist[1] = (Integer.parseInt(String.valueOf(move.charAt(1))));
        return returnlist;

    }

    private static void hilightLegalMoves(ChessPosition position) {
    }

    public static void playRepl(UserGameCommand command, Boolean isPlayer) throws Exception {
        //if isPlayer is false then he is an observer

        Scanner scanner = new Scanner(System.in);
        gson = new Gson();
        if (!isPlayer) {
            helper = new WsHelper(8080, command, false);
        } else if (isPlayer) {
            helper = new WsHelper(8080, command, true);

        }
        String connectJson = gson.toJson(command);
        helper.send(connectJson);
//        drawboard(command,helper);
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
                       makeMoveHelper(command);
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
    private static void makeMoveHelper(UserGameCommand command) throws IOException {

        Scanner scanner = new Scanner(System.in);
        // eventually you need to add a way for the user to give their own move
        System.out.println("What move do you want to make (start) (end) (pawn promotion)?");
        String moveString = scanner.nextLine().trim().toLowerCase();
        String[] moveParts = moveString.split(" ");
        String startingPosition = moveParts[0];
        String endPosition = moveParts [1];
        String promoteString=null;
        ChessPiece.PieceType promotionPeice = null;
        var movePair = makeMoveHelper(startingPosition);
        if ((0 > movePair[0] || movePair[0] > 9) ||  (0 > movePair[1] || movePair[1] > 9)) {

            return;
        }
        var endPair = makeMoveHelper(endPosition);
        if ((0 > endPair[0] || endPair[0] > 9) || (0 > endPair[1] || endPair[1] > 9)) {

            return;
        }
        if(moveParts.length==3){
            promoteString = moveParts[2];
            switch (promoteString){
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
        makeMove(new ChessMove(new ChessPosition(movePair[0],movePair[1]),
                new ChessPosition(endPair[0],endPair[1]),
                promotionPeice),command,helper);
    }

}
