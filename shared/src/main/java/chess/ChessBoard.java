package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {

    }


    @Override
    public ChessBoard clone() {
        ChessBoard copy = new ChessBoard();
        copy.board = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.board[i], 0, copy.board[i], 0, 8);
        }

        return copy;
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
     * @param pos   where to add the piece to
     * @param piece the piece to add
     */
    public void addPiece(ChessPosition pos, ChessPiece piece) {
        //this could also potentially be used to move a piece
        board[pos.getRow() - 1][pos.getColumn() - 1] = piece;
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param pos The pos to get the piece from
     * @return Either the piece at the pos, or null if no piece is at that
     * pos
     */
    public ChessGame.TeamColor getColor(ChessPosition pos) {

        ChessPiece piece = getPiece(pos);
        if (piece != null) {
            return piece.getTeamColor();
        }
        return null;
    }

    public ChessPiece getPiece(ChessPosition pos) {

        return board[pos.getRow() - 1][pos.getColumn() - 1];
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = getPiece(pos);

                if (piece == null) {
                    continue;
                }
                if (piece.getPieceType() != ChessPiece.PieceType.KING) {
                    continue;
                }
                if (piece.getTeamColor() != teamColor) {
                    continue;
                }

                return pos;
            }
        }

        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        int[] bishopColumns = {3, 6};
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        int[] knightColumns = {2, 7};
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        int[] rookColumns = {1, 8};
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        for (int i = 1; i < 9; i++) {
            ChessPosition whitepos = new ChessPosition(2, i);
            ChessPosition blackpos = new ChessPosition(7, i);
            addPiece(whitepos, whitePawn);
            addPiece(blackpos, blackPawn);
        }
        for (int pos : bishopColumns) {
            ChessPosition whitepos = new ChessPosition(1, pos);
            ChessPosition blackpos = new ChessPosition(8, pos);
            addPiece(whitepos, whiteBishop);
            addPiece(blackpos, blackBishop);
        }
        for (int pos : rookColumns) {
            ChessPosition whitepos = new ChessPosition(1, pos);
            ChessPosition blackpos = new ChessPosition(8, pos);
            addPiece(whitepos, whiteRook);
            addPiece(blackpos, blackRook);
        }
        for (int pos : knightColumns) {
            ChessPosition whitepos = new ChessPosition(1, pos);
            ChessPosition blackpos = new ChessPosition(8, pos);
            addPiece(whitepos, whiteKnight);
            addPiece(blackpos, blackKnight);
        }
        ChessPosition whitepos = new ChessPosition(1, 5);
        ChessPosition blackpos = new ChessPosition(8, 5);
        addPiece(whitepos, whiteKing);
        addPiece(blackpos, blackKing);
        whitepos = new ChessPosition(1, 4);
        blackpos = new ChessPosition(8, 4);
        addPiece(whitepos, whiteQueen);
        addPiece(blackpos, blackQueen);
    }
}

