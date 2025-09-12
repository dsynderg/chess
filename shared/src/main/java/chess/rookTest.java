import chess.*;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
public Collection<ChessMove> lat_vert(ChessPosition pos, ChessBoard board){
    //directions 0 is north 1 is east 2 is west and 3 is south
    //This could also be used in some way to test king danger
    List<ChessMove> rookMoveList = new ArrayList<>();

    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int row = pos.getRow();
    int moverRow = row;
    int col = pos.getColumn();
    int moverCol = col;
    ChessGame.TeamColor square_piece_color = null;
    //make a while loop, whil the position isn't negative && board position doesn't have a pice on it
    //while right
    while (moverCol<8 && square_piece_color==null){
        moverCol++;
        ChessPosition position = new ChessPosition(row,moverCol);
        if(board.getPiece(position)!=null){
            square_piece_color = board.getColor(position);
        }

        if (square_piece_color != pieceColor){
            rookMoveList.add(new ChessMove(pos,new ChessPosition(row,moverCol),null));
        }
    }
    moverCol = col;
    square_piece_color = null;
    //while left
    while (moverCol>1 && square_piece_color==null){
        moverCol--;
        ChessPosition position = new ChessPosition(row,moverCol);
        if(board.getPiece(position)!=null){
            square_piece_color = board.getColor(position);
        }

        if (square_piece_color != pieceColor){
            rookMoveList.add(new ChessMove(pos,new ChessPosition(row,moverCol),null));
        }
    }
    //while up
    while (moverRow<8 && square_piece_color==null){
        moverRow++;
        ChessPosition position = new ChessPosition(moverRow,col);
        if(board.getPiece(position)!=null){
            square_piece_color = board.getColor(position);
        }

        if (square_piece_color != pieceColor){
            rookMoveList.add(new ChessMove(pos,new ChessPosition(moverRow,col),null));
        }
    }
    moverRow = row;
    square_piece_color = null;
    //while down
    while (moverRow>1 && square_piece_color==null){
        moverRow--;
        ChessPosition position = new ChessPosition(moverRow,col);
        if(board.getPiece(position)!=null){
            square_piece_color = board.getColor(position);
        }

        if (square_piece_color != pieceColor){
            rookMoveList.add(new ChessMove(pos,new ChessPosition(moverRow,col),null));
        }
    }
    return rookMoveList;
}
public class Main{
    public static void main(String[] args){

    }
}

void main() {
    ChessPosition pos = new ChessPosition(4,4);
    ChessBoard board = new ChessBoard();
    Collection<ChessMove> moves = lat_vert(pos,board);
    for (ChessMove move : moves){
        System.out.println(move);
    }
    System.out.println("--------");
    ChessPiece enemy_peice = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK);
    board.addPiece(new ChessPosition(5,4),enemy_peice);
    moves = lat_vert(pos,board);
    for (ChessMove move : moves){
        System.out.println(move);
    }
}