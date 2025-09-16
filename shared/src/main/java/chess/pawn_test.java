import chess.*;

public Collection<ChessMove> pawn_move(ChessPosition pos, ChessBoard board){
    /**
     * We need a way to identify the color of the pawn
     * if its black it has to move down
     * if white, it has to move up
     * we need a check to see if the pawn has already moved
     * check if there are enemys to the left and rigth
     */
    List<ChessMove> moves = new ArrayList<>();
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int row = pos.getRow();
    int moverrow = row;
    int col = pos.getColumn();
    int movercol =col;
    ChessGame.TeamColor square_peice_color = null;
    int[] white_advance = {1,0};
    int[] black_advance = {-1,0};
    int[][] white_attack = {{1,-1},{1,1}};
    int[][] black_attack = {{-1,-1},{-1,1}};
    if(pieceColor==ChessGame.TeamColor.WHITE){
        moverrow += white_advance[0];
        ChessPosition new_position = new ChessPosition(moverrow,movercol);
        if(board.getColor(new_position)==null){
            moves.add(new ChessMove(pos,new_position,null));
        }
    }


    return moves;
}

public class Main {
    public static void main(String[] args){
    
    }
}
void main (){

}