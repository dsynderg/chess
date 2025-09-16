import chess.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
public Collection<ChessMove> white_advance(ChessPosition pos,ChessBoard board){
    int row = pos.getRow();
    int moverrow = row;
    int col = pos.getColumn();
    int movercol =col;
    
    ChessGame.TeamColor square_peice_color = null;
    int[] white_advance = {1,0};
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int[] attacks = {-1,2,};
    List<ChessMove> moves = new ArrayList<>();
    ChessPiece.PieceType [] possible_promotions = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN};

    moverrow += white_advance[0];
    ChessPosition new_position = new ChessPosition(moverrow,movercol);
    if(board.getColor(new_position)==null){
        if(new_position.getRow()==8){
            for(ChessPiece.PieceType PromotionPiece: possible_promotions) {
                moves.add(new ChessMove(pos, new_position, promotionPiece));
            }
        }
        else{
            moves.add(new ChessMove(pos,new_position,null));}
    }
    if(row == 2){
        moverrow += white_advance[0];
        new_position = new ChessPosition(moverrow,movercol);
        if(board.getColor(new_position)==null){
            moves.add(new ChessMove(pos,new_position,null));}
        moverrow -=1;
    }
    for(int attack:attacks){
        movercol+=attack;
        new_position = new ChessPosition(moverrow,movercol);
        if(board.getColor(new_position)== ChessGame.TeamColor.BLACK) {
            if (new_position.getRow() == 8) {
                moves.add(new ChessMove(pos, new_position, promotionPiece));
            } else {
                moves.add(new ChessMove(pos, new_position, null));

            }
        }


    }
    return moves;
}
public Collection<ChessMove> black_advance(ChessPosition pos,ChessBoard board){
    int row = pos.getRow();
    int moverrow = row;
    int col = pos.getColumn();
    int movercol =col;
    ChessPiece.PieceType promotionPiece=null;
    ChessGame.TeamColor square_peice_color = null;
    int[] black_advance = {-1,0};
    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
    int[] attacks = {-1,2,};
    List<ChessMove> moves = new ArrayList<>();

    moverrow += black_advance[0];
    ChessPosition new_position = new ChessPosition(moverrow,movercol);
    if(board.getColor(new_position)==null){
        if(new_position.getRow()==1){
            moves.add(new ChessMove(pos,new_position,promotionPiece));
        }
        else{
            moves.add(new ChessMove(pos,new_position,null));}
    }
    if(row == 7){
        moverrow += black_advance[0];
        new_position = new ChessPosition(moverrow,movercol);
        if(board.getColor(new_position)==null){
            moves.add(new ChessMove(pos,new_position,null));}
        moverrow -=1;
    }
    for(int attack:attacks){
        movercol+=attack;
        new_position = new ChessPosition(moverrow,movercol);
        if(board.getColor(new_position)== ChessGame.TeamColor.WHITE) {
            if (new_position.getRow() == 1) {
                moves.add(new ChessMove(pos, new_position, promotionPiece));
            } else {
                moves.add(new ChessMove(pos, new_position, null));

            }
        }

    }
    return moves;
}
public Collection<ChessMove> pawn_move(ChessPosition pos, ChessBoard board){
    /**
     * We need a way to identify the color of the pawn
     * if its black it has to move down
     * if white, it has to move up
     * we need a check to see if the pawn has already moved
     * check if there are enemys to the left and rigth
     */

    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;




    if(pieceColor==ChessGame.TeamColor.WHITE){
        return white_advance(pos,board);
    }
    else {
        return black_advance(pos,board);
    }



}

public class Main {
    public static void main(String[] args){
    
    }
}
void main (){
    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    ChessPiece friend = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    ChessBoard board = new ChessBoard();
    ChessPosition pos = new ChessPosition(2,2);
//    board.addPiece(new ChessPosition(4,1),friend);
    board.addPiece(new ChessPosition(3,3),enemy);
//    board.addPiece(new ChessPosition(2,5),friend);
    Collection<ChessMove> moves = pawn_move(pos,board);
    for(ChessMove move :moves){
        System.out.println(move);
    }
}