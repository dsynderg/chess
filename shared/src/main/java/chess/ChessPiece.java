package chess;

import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> valid_moves(){
        /**
         * use recursion on all of the peices that can be blocked, check if there is a valid peice
         * if so, return the list of all possible moves, if not check the next possible square
         * but for every peice have a set of rules that everything must follow:
         *  cant move on its own peices
         *  cant move off the board
         */
    }
    public Collection<ChessMove> knights_move(ChessPosition pos){
        /**you need a starting position of the peice this could be on the Chess peice class
         * then it will move 2 in any four directions
         * and 1 in an orthoginal direction
         *
         */
    }
    public Collection<ChessMove> rooks_move(ChessPosition pos){

    }
    public Collection<ChessMove> bishops_move(ChessPosition pos){

    }
    public Collection<ChessMove> queens_move(ChessPosition pos){

    }
    public Collection<ChessMove> kings_move(ChessPosition pos){

    }
    public Collection<ChessMove> pawns_move(ChessPosition pos){
        chess.ChessPosition position = chess.ChessMove.getStartPosition();

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.KING) {

        }
        else if (type == PieceType.QUEEN) {}
        else if (type == PieceType.ROOK) {}
        else if (type == PieceType.KNIGHT) {}
        else if (type == PieceType.BISHOP) {}
        else if (type == PieceType.PAWN) {}
        return Collections.emptyList();

        }
    }


