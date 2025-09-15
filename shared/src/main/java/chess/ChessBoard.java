package chess;

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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
