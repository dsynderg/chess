import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public Collection<ChessMove> bishops_move(ChessPosition pos, ChessBoard board){
    List<ChessMove> moves = new ArrayList<>();
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int[] northwest = {1,-1};
    int[] northeast = {1,1};
    int[] southwest = {-1,-1};
    int[] southeast = {-1,1};
    int[][] directions = new int[][]{northwest, northeast, southwest, southeast};
    int row = pos.getRow();
    int moverRow = row;
    int col = pos.getColumn();
    int moverCol = col;
    ChessGame.TeamColor square_piece_color=null;
    for(int[] direction:directions){
        int up = direction[0];
        int right = direction[1];
        while(moverRow+up<9 &&(moverRow+up)>0 && moverCol+right<9 && moverCol+right>0
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
public class Main{
    public static void main(String[] args){

    }
}
void main() {
    int[] northwest = {1,-1};
    int[] northeast = {1,1};
    int[] southwest = {-1,-1};
    int[] southeast = {-1,1};
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    ChessPiece friend = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();
    ChessPosition pos = new ChessPosition(5,2);
    board.addPiece(new ChessPosition(4,1),friend);
    board.addPiece(new ChessPosition(7,4),enemy);
    board.addPiece(new ChessPosition(2,5),friend);
    Collection<ChessMove> moves = bishops_move(pos,board);
    for(ChessMove move :moves){
        System.out.println(move);
    }

        }