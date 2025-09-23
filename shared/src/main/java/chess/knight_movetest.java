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
    int [][] possible_moves = {{-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1}};
    for(int[] move : possible_moves){
        if(row-move[0]>1&&row-move[0]<8&&col - move[1]>1 && col - move[1]<8) {
            ChessPosition new_pos = new ChessPosition(row - move[0], col - move[1]);
            square_piece_color = board.getColor(new_pos);
            if (square_piece_color != pieceColor) {
                ChessMove new_move = new ChessMove(pos, new_pos, null);
                knightMoveList.add(new_move);
            }
        }
    }


    return knightMoveList;

}
public class Main{
    public static void main(String[] args){

    }
}

void main() {
    ChessPosition pos = new ChessPosition(1,1);
    ChessPosition enemypos = new ChessPosition(6,3);
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();
    board.addPiece(enemypos,enemy);

    Collection<ChessMove> moves = knights_move_preview(pos,board);
    for(ChessMove move : moves){
        System.out.println(move);
    }
}