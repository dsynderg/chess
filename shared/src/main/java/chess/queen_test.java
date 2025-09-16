import chess.*;

import java.util.Collection;
import java.util.List;
import java. util.ArrayList;

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

public Collection<ChessMove> bishops_move(ChessPosition pos, ChessBoard board){
    List<ChessMove> moves = new ArrayList<>();
    int[] northwest = {1,-1};
    int[] northeast = {1,1};
    int[] southwest = {-1,-1};
    int[] southeast = {-1,1};
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int[][] directions = new int[][]{northwest, northeast, southwest, southeast};
    int row = pos.getRow();
    int moverRow = row;
    int col = pos.getColumn();
    int moverCol = col;
    ChessGame.TeamColor square_piece_color=null;
    for(int[] direction:directions){
        int up = direction[0];
        int right = direction[1];
        while(moverRow<8 && moverRow>1 && moverCol<8 && moverCol>1
                && square_piece_color==null){
            moverRow+=up;
            moverCol+=right;
            ChessPosition new_position = new ChessPosition(moverRow,moverCol);
            if(board.getPiece(new_position)!=null){
                square_piece_color = board.getColor((new_position));
            }
            if (square_piece_color != pieceColor){
                moves.add(new ChessMove(pos,new_position,null));
            }
        }
        moverRow = row;
        moverCol = col;
        square_piece_color = null;

    }


    return moves;
}
public Collection<ChessMove> queen_moves(ChessPosition pos, ChessBoard board){

    Collection<ChessMove> diagonal_moves = bishops_move(pos,board);
//    Collection<ChessMove> latteral_moves = lat_vert(pos,board);
//    latteral_moves.addAll(diagonal_moves);
    return diagonal_moves;

}
public class Main{
    public static void main(String[] args){

    }
}
void main() {
    ChessPosition pos = new ChessPosition(4,1);
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    ChessPiece friend = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();

    board.addPiece(new ChessPosition(2,1),friend);
    board.addPiece(new ChessPosition(6,3),friend);
    board.addPiece(new ChessPosition(2,3),enemy);
    board.addPiece(new ChessPosition(4,3),enemy);
    board.addPiece(new ChessPosition(8,1),enemy);
    Collection<ChessMove> moves = queen_moves(pos,board);
    for (ChessMove move : moves){
        System.out.println(move);
    }

}