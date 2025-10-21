//import chess.*;
//import java.util.Collection;
//import java.util.List;
//import java.util.ArrayList;
//public Collection<ChessMove> pawn_move(ChessPosition pos,ChessBoard board,ChessGame.TeamColor pieceColor){
//    int row = pos.getRow();
//    int moverrow = row;
//    int col = pos.getColumn();
//    int movercol =col;
//
//    ChessGame.TeamColor square_peice_color = null;
//    int[] advance;
//    int promotion_rank;
//    int start_rank;
//    if(pieceColor == ChessGame.TeamColor.WHITE){
//        advance = new int[]{1,0};
//        promotion_rank = 8;
//        start_rank = 2;
//
//    }
//    else {
//        advance = new int[]{-1,0};
//        promotion_rank = 1;
//        start_rank = 7;
//    }
//
//    int[] attacks = {-1,2,};
//    List<ChessMove> moves = new ArrayList<>();
//    ChessPiece.PieceType [] possible_promotions = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN};
//
//    moverrow += advance[0];
//    ChessPosition new_position = new ChessPosition(moverrow,movercol);
//    if(board.getColor(new_position)==null){
//        if(new_position.getRow()==promotion_rank){
//            for(ChessPiece.PieceType PromotionPiece: possible_promotions) {
//                moves.add(new ChessMove(pos, new_position, PromotionPiece));
//            }
//        }
//        else{
//            moves.add(new ChessMove(pos,new_position,null));}
//    }
//    if(row == start_rank){
//        moverrow += advance[0];
//        ChessPosition old_position = new_position;
//        new_position = new ChessPosition(moverrow,movercol);
//        if(board.getColor(new_position)==null&&board.getColor(old_position)==null){
//            moves.add(new ChessMove(pos,new_position,null));}
//        moverrow -=1;
//    }
//    for(int attack:attacks){
//        movercol+=attack;
//        new_position = new ChessPosition(moverrow,movercol);
//        if(board.getColor(new_position)!= pieceColor && board.getColor(new_position)!=null) {
//            if (new_position.getRow() == promotion_rank) {
//                for(ChessPiece.PieceType PromotionPiece: possible_promotions) {
//                    moves.add(new ChessMove(pos, new_position, PromotionPiece));
//                }
//            } else {
//                moves.add(new ChessMove(pos, new_position, null));
//
//            }
//        }
//
//
//    }
//    return moves;
//}
//public Collection<ChessMove> black_advance(ChessPosition pos,ChessBoard board){
//    int row = pos.getRow();
//    int moverrow = row;
//    int col = pos.getColumn();
//    int movercol =col;
//    ChessPiece.PieceType promotionPiece=null;
//    ChessGame.TeamColor square_peice_color = null;
//    int[] black_advance = {-1,0};
//    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
//    int[] attacks = {-1,2,};
//    List<ChessMove> moves = new ArrayList<>();
//    ChessPiece.PieceType [] possible_promotions = {ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN};
//
//    moverrow += black_advance[0];
//    ChessPosition new_position = new ChessPosition(moverrow,movercol);
//    if(board.getColor(new_position)==null){
//        if(new_position.getRow()==1){
//            for(ChessPiece.PieceType PromotionPiece: possible_promotions) {
//                moves.add(new ChessMove(pos, new_position, PromotionPiece));
//            }
//        }
//        else{
//            moves.add(new ChessMove(pos,new_position,null));}
//    }
//    if(row == 7){
//        moverrow += black_advance[0];
//        new_position = new ChessPosition(moverrow,movercol);
//        if(board.getColor(new_position)==null){
//            moves.add(new ChessMove(pos,new_position,null));}
//        moverrow -=1;
//    }
//    for(int attack:attacks){
//        movercol+=attack;
//        new_position = new ChessPosition(moverrow,movercol);
//        if(board.getColor(new_position)== ChessGame.TeamColor.WHITE) {
//            if (new_position.getRow() == 1) {
//                for(ChessPiece.PieceType PromotionPiece: possible_promotions) {
//                    moves.add(new ChessMove(pos, new_position, PromotionPiece));
//                }
//            } else {
//                moves.add(new ChessMove(pos, new_position, null));
//
//            }
//        }
//
//    }
//    return moves;
//}
//public Collection<ChessMove> pawn_move(ChessPosition pos, ChessBoard board){
//    /**
//     * We need a way to identify the color of the pawn
//     * if its black it has to move down
//     * if white, it has to move up
//     * we need a check to see if the pawn has already moved
//     * check if there are enemys to the left and rigth
//     */
//
//    ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
//
//
//
//
//    return pawn_move(pos,board,pieceColor);
//
//
//
//}
//
//public class Main {
//    public static void main(String[] args){
//
//    }
//}
//void main (){
//    ChessPiece enemy = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
//    ChessPiece friend = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
//    ChessBoard board = new ChessBoard();
//    ChessPosition pos = new ChessPosition(2,2);
/// /    board.addPiece(new ChessPosition(4,1),friend);
//    board.addPiece(new ChessPosition(3,3),enemy);
////    board.addPiece(new ChessPosition(2,5),friend);
//    Collection<ChessMove> moves = pawn_move(pos,board);
//    for(ChessMove move :moves){
//        System.out.println(move);
//    }
//}