package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    //    private ChessPosition pos;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
//        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        if (this == null) {
            return null;
        }
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
//    public Collection<ChessMove> valid_moves(){
//        /**
//         * use recursion on all the peices that can be blocked, check if there is a valid peice
//         * if so, return the list of all possible moves, if not check the next possible square
//         * but for every peice have a set of rules that everything must follow:
//         *  cant move on its own peices
//         *  cant move off the board
//         */
//    }
    private Boolean kingHelper(ChessBoard board, Collection<ChessMove> dangerMoves, List<PieceType> danger_pieces) {
        for (ChessMove move : dangerMoves) {
            ChessPiece enemyPiece = board.getPiece(move.getEndPosition());
            if (enemyPiece != null) {
                if (danger_pieces.contains(enemyPiece.getPieceType())) {
                    if (enemyPiece.getTeamColor() != pieceColor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean isKingInCheck(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> dangerMoves;
        int[][] rookdirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        int[][] bishopdirections = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        int[][] knightdirections = {{2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}};
        int[][] kingdirections = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        dangerMoves = queenRookBishopMove(pos, board, rookdirections);
        if (kingHelper(board, dangerMoves, new ArrayList<>(Arrays.asList(PieceType.ROOK, PieceType.QUEEN)))) {
            return true;
        }
        dangerMoves = queenRookBishopMove(pos, board, bishopdirections);
        if (kingHelper(board, dangerMoves, new ArrayList<>(Arrays.asList(PieceType.BISHOP, PieceType.QUEEN)))) {
            return true;
        }
        dangerMoves = knightKingMoves(pos, board, knightdirections);
        if (kingHelper(board, dangerMoves, new ArrayList<>(List.of(PieceType.KNIGHT)))) {
            return true;
        }
        dangerMoves = knightKingMoves(pos, board, kingdirections);
        if (kingHelper(board, dangerMoves, new ArrayList<>(List.of(PieceType.KING)))) {
            return true;
        }
        //still need to implement pawn danger
        int moverrow = pos.getRow();
        int movercol = pos.getColumn();
//        Collection<ChessMove> moves = new ArrayList<>();
        int[] attacks = {-1, 2};
        int advance;
        ChessGame.TeamColor enemyColor;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            advance = 1;
            enemyColor = ChessGame.TeamColor.BLACK;
        } else {
            advance = -1;
            enemyColor = ChessGame.TeamColor.WHITE;
        }
        moverrow += advance;
        for (int attack : attacks) {

            movercol += attack;
            if (movercol >= 1 && movercol <= 8 && moverrow >= 1 && moverrow <= 8) {
                ChessPosition attackPos = new ChessPosition(moverrow, movercol);
//                if(movercol)
                ChessPiece piece = board.getPiece(attackPos);
                if (board.getColor(attackPos) == enemyColor && piece.getPieceType() == PieceType.PAWN) {
                    return true;
                }
            }
        }
        //queen and rookcheck;

        return false;
    }

    private Collection<ChessMove> queenRookBishopMove(ChessPosition pos, ChessBoard board, int[][] directions) {
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor squareColor = null;
        for (int[] direction : directions) {
            int autotricker = 0;
            while (moverrow >= 1 && moverrow <= 8 && movercol >= 1 && movercol <= 8 && squareColor == null) {
                moverrow += direction[0];
                movercol += direction[1];
                if (moverrow > 0 && moverrow < 9 && movercol > 0 && movercol < 9) {
                    ChessPosition newPos = new ChessPosition(moverrow, movercol);
                    squareColor = board.getColor(newPos);
                     autotricker = 5;
                    if (squareColor != pieceColor) {
                        moves.add(new ChessMove(pos, newPos, null));
                        autotricker++;
                    }
                }
            }
            int as = autotricker+1;
            moverrow = row;
            movercol = col;
            squareColor = null;
            autotricker = as;

        }
        return moves;
    }

    private Collection<ChessMove> knightKingMoves(ChessPosition pos, ChessBoard board, int[][] directions) {
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor squareColor = null;
        for (int[] direction : directions) {

            moverrow += direction[0];
            movercol += direction[1];
            if (moverrow > 0 && moverrow < 9 && movercol > 0 && movercol < 9) {
                ChessPosition newPos = new ChessPosition(moverrow, movercol);
                squareColor = board.getColor(newPos);
                if (squareColor != pieceColor) {
                    moves.add(new ChessMove(pos, newPos, null));
                }

            }
            moverrow = row;
            movercol = col;
            squareColor = null;
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessPosition pos, ChessBoard board) {
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        int[] attacks = {-1, 2};
        int advance, promo, start;
        ChessGame.TeamColor enemy_color;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            advance = 1;
            promo = 8;
            start = 2;
            enemy_color = ChessGame.TeamColor.BLACK;
        } else {
            advance = -1;
            promo = 1;
            start = 7;
            enemy_color = ChessGame.TeamColor.WHITE;
        }
        ChessGame.TeamColor squareColor = null;
        ChessPosition newPos = new ChessPosition(row + advance, col);
        if (board.getColor(newPos) == null) {
            if (newPos.getRow() == promo) {
                for (ChessPiece.PieceType promotion : promotions) {
                    moves.add(new ChessMove(pos, newPos, promotion));
                }
            } else {
                moves.add(new ChessMove(pos, newPos, null));
                if (row == start) {
                    ChessPosition doubleAdvance = new ChessPosition(row + advance + advance, col);
                    if (board.getColor(doubleAdvance) == null) {
                        moves.add(new ChessMove(pos, doubleAdvance, null));
                    }
                }
            }


        }
        moverrow += advance;
        for (int attack : attacks) {

            movercol += attack;
            if (movercol >= 1 && movercol <= 8) {
                ChessPosition attackPos = new ChessPosition(moverrow, movercol);

                if (board.getColor(attackPos) == enemy_color) {
                    if (moverrow == promo) {
                        for (ChessPiece.PieceType promotion : promotions) {
                            moves.add(new ChessMove(pos, attackPos, promotion));

                        }
                    } else {
                        moves.add(new ChessMove(pos, attackPos, null));

                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = List.of();
        if (type == PieceType.KING) {
            int[][] directions = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};
            return knightKingMoves(myPosition, board, directions);
        }
        if (type == PieceType.QUEEN) {
            int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            return queenRookBishopMove(myPosition, board, directions);
        }
        if (type == PieceType.KNIGHT) {
            int[][] directions = {{2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}};
            return knightKingMoves(myPosition, board, directions);
        }
        if (type == PieceType.ROOK) {
            int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            return queenRookBishopMove(myPosition, board, directions);
        }
        if (type == PieceType.BISHOP) {
            int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
            return queenRookBishopMove(myPosition, board, directions);

        }
        if (type == PieceType.PAWN) {
            return pawnMoves(myPosition, board);
        }
        return moves;
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
}


