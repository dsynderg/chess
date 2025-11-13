package services;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class BoardPrinter {
    public static void printBoard(ChessBoard board, ChessGame.TeamColor viewPosition){
        String topbottom = (viewPosition == ChessGame.TeamColor.WHITE) ? "    A  B  C  D  E  F  G  H    " : "    H  G  F  E  D  C  B  A    ";

        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(topbottom);
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for(int i=0;i<8;i++){
            int row = (viewPosition == ChessGame.TeamColor.WHITE) ? 8 - i : i + 1;
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" "+String.valueOf(row)+" ");
            for(int j=0;j<8;j++){


                int col = (viewPosition == ChessGame.TeamColor.WHITE) ? j + 1 : 8 - j;
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                //if its an even square
                boolean isEvenSquare = (i + j) % 2 == 0;
                String bgColor = isEvenSquare ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                String textColor;


                if (piece != null) {
                    textColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_RED : EscapeSequences.SET_TEXT_COLOR_BLUE;
                    System.out.print(textColor + bgColor + " " + piece + " ");
                } else {
                    textColor = isEvenSquare ? EscapeSequences.SET_TEXT_COLOR_WHITE : EscapeSequences.SET_TEXT_COLOR_BLACK;
                    System.out.print(bgColor + textColor + " a ");

                }


            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" "+String.valueOf(row)+" ");

            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(topbottom);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.RESET_BG_COLOR);


    }

}
