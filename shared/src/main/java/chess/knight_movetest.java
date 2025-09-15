import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public Collection<ChessMove> knights_move_preview(ChessPosition pos, ChessBoard board){
    /**you need a starting position of the peice this could be on the Chess peice class
     * then it will move 2 in any four directions
     * and 1 in an orthoginal direction
     *
     */
    List<ChessMove> knightMoveList = new ArrayList<>();
    //offset is used to cycle left and right, up and down, to make the L shape

    int row = pos.getRow();
    int col = pos.getColumn();
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    ChessGame.TeamColor square_piece_color = null;
    int[] offset = {-1,1};
    int[] onset = {2,-2};
    for (int off : offset){
        for (int on : onset){
            ChessPosition endpos = new ChessPosition(row+on,col+off);
            square_piece_color = board.getColor(endpos);
            if (pieceColor!=square_piece_color) {
                knightMoveList.add(new ChessMove(pos, endpos, null));
            }
            square_piece_color=null;
        }
    }
    for (int off : offset){
        for (int on : onset){
            ChessPosition endpos = new ChessPosition(row+off,col+on);
            square_piece_color = board.getColor(endpos);
            if (pieceColor!=square_piece_color) {
                knightMoveList.add(new ChessMove(pos, endpos, null));
            }
            square_piece_color = null;
        }
    }
    return knightMoveList;

}
public class Main{
    public static void main(String[] args){

    }
}

void main() {
    ChessPosition pos = new ChessPosition(4,4);
    ChessPosition enemypos = new ChessPosition(6,3);
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();
    board.addPiece(enemypos,enemy);

    Collection<ChessMove> moves = knights_move_preview(pos,board);
    for(ChessMove move : moves){
        System.out.println(move);
    }
}