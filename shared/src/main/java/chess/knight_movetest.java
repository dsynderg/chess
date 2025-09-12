import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public Collection<ChessMove> knights_move_preview(ChessPosition pos){
    /**you need a starting position of the peice this could be on the Chess peice class
     * then it will move 2 in any four directions
     * and 1 in an orthoginal direction
     *
     */
    List<ChessMove> knightMoveList = new ArrayList<>();
    //offset is used to cycle left and right, up and down, to make the L shape

    int row = pos.getRow();
    int col = pos.getColumn();
    int[] offset = {-1,1};
    int[] onset = {2,-2};
    for (int off : offset){
        for (int on : onset){
            ChessPosition endpos = new ChessPosition(row+on,col+off);
            knightMoveList.add(new ChessMove(pos,endpos,null));
        }
    }
    for (int off : offset){
        for (int on : onset){
            ChessPosition endpos = new ChessPosition(row+off,col+on);
            knightMoveList.add(new ChessMove(pos,endpos,null));
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
    Collection<ChessMove> moves = knights_move_preview(pos);
    for(ChessMove move : moves){
        System.out.println(move);
    }
}