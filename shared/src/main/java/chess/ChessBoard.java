package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    final private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param pos where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition pos, ChessPiece piece) {
    //this could also potentially be used to move a piece
        board[pos.getRow()-1][pos.getColumn()-1] = piece;
    }
    public void removePiece(ChessPosition pos){
        board[pos.getRow()-1][pos.getColumn()-1]=null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param pos The pos to get the piece from
     * @return Either the piece at the pos, or null if no piece is at that
     * pos
     */
    public ChessGame.TeamColor getColor(ChessPosition pos){

        ChessPiece piece = getPiece(pos);
        if (piece != null) {
            return piece.getTeamColor();
        }
        return null;
    }
    public ChessPiece getPiece(ChessPosition pos) {

        return board[pos.getRow()-1][pos.getColumn()-1];
    }
    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor){
        for(int i = 0;i<8;i++){
            for (int j = 0; j<8;j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = getPiece(pos);
                if(piece.getPieceType()== ChessPiece.PieceType.KING){
                    if(piece.getTeamColor() == teamColor){
                        return pos;
                    }

                }
            }
        }
        return null;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece black_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece black_bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        int[] bishop_columns = {3,6};
        ChessPiece black_knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        int[] knight_columns = {2,7};
        ChessPiece black_rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        int[] rook_columns = {1,8};
        ChessPiece black_king = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece black_queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece white_bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece white_knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece white_rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece white_king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece white_queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        for(int i=1;i<9;i++){
            ChessPosition whitepos = new ChessPosition(2,i);
            ChessPosition blackpos = new ChessPosition(7,i);
            addPiece(whitepos,white_pawn);
            addPiece(blackpos,black_pawn);
       }
        for(int pos:bishop_columns){
            ChessPosition whitepos = new ChessPosition(1,pos);
            ChessPosition blackpos = new ChessPosition(8,pos);
            addPiece(whitepos,white_bishop);
            addPiece(blackpos,black_bishop);
        }
        for(int pos:rook_columns){
            ChessPosition whitepos = new ChessPosition(1,pos);
            ChessPosition blackpos = new ChessPosition(8,pos);
            addPiece(whitepos,white_rook);
            addPiece(blackpos,black_rook);
        }
        for(int pos:knight_columns){
            ChessPosition whitepos = new ChessPosition(1,pos);
            ChessPosition blackpos = new ChessPosition(8,pos);
            addPiece(whitepos,white_knight);
            addPiece(blackpos,black_knight);
        }
        ChessPosition whitepos = new ChessPosition(1,5);
        ChessPosition blackpos = new ChessPosition(8,5);
        addPiece(whitepos,white_king);
        addPiece(blackpos,black_king);
        whitepos = new ChessPosition(1,4);
        blackpos = new ChessPosition(8,4);
        addPiece(whitepos,white_queen);
        addPiece(blackpos,black_queen);
    }
    }

