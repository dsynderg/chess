import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public Collection<ChessMove> bishops_move(ChessPosition pos, ChessBoard board){
    List<ChessMove> moves;
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
        while(moverRow<8 && moverRow>1 && moverCol<8 && moverCol>1
        && square_piece_color==null){
            moverRow+=up;
            moverCol+=right;
            ChessPosition new_position = new ChessPosition(moverRow,moverCol);
            if(board.getPiece(new_position)!=null){
                square_piece_color = board.getColor((new_position));
            }
            if (square_piece_color != pieceColor){
                moves.add(ChessMove(pos,new_position,null));
            }
        }
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
    int[][] directions = new int[][]{northwest, northeast, southwest, southeast};
    for(int[] direction:directions){
        System.out.println(direction[0]);
    }
        }