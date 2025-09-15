import chess.*;

import java.util.Collection;
import java.util.ArrayList;

public Collection<ChessMove> kings_move(ChessPosition pos, ChessBoard board){
    Collection<ChessMove> moves = new ArrayList<>();
    int[][] possible_moves = {{-1,1},{0,1},{1,1},{-1,0},{1,0},{-1,-1},{-1,0},{-1,1}};
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    ChessGame.TeamColor square_piece_color = null;
    for(int[] move :possible_moves){
        ChessPosition new_position = new ChessPosition(pos.getRow()+move[0],pos.getColumn()+move[1]);
        if(board.getPiece(new_position)!=null){
            square_piece_color = board.getColor(new_position);
        }
        if(square_piece_color!=pieceColor){
            moves.add(new ChessMove(pos,new_position,null));
        }
        square_piece_color = null;
    }
    return moves;
}
public class Main {
    public static void main(String[] args){

    }
}
void main() {
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    ChessPiece friend = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();
    ChessPosition pos = new ChessPosition(4,4);
    board.addPiece(new ChessPosition(5,5),enemy);
    Collection<ChessMove> moves = kings_move(pos,board);
    for(ChessMove move :moves){
        System.out.println(move);
    }
}