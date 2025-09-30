package chess;

import java.awt.geom.NoninvertibleTransformException;
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
        if (this ==null){
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
    private Boolean king_helper (ChessBoard board,Collection<ChessMove> danger_moves, List<PieceType> danger_pieces){
        for(ChessMove move: danger_moves){
            ChessPiece enemy_piece = board.getPiece(move.getEndPosition());
            if(enemy_piece!=null) {
                if (danger_pieces.contains(enemy_piece.getPieceType())) {
                    if (enemy_piece.getTeamColor() != pieceColor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean isKingInCheck(ChessBoard board, ChessPosition pos){
        Collection<ChessMove> danger_moves;
        int[][] rookdirections={{1,0},{0,1},{-1,0},{0,-1}};
        int[][] bishopdirections={{1,-1},{1,1},{-1,-1},{-1,1}};
        int[][] knightdirections={{2,-1},{1,-2},{-1,-2}, {-2, -1},{-2,1},{-1,2},{1,2},{2,1}};
        danger_moves = Queen_Rook_bishop_move(pos,board,rookdirections);
        if(king_helper(board,danger_moves,new ArrayList<>(Arrays.asList(PieceType.ROOK,PieceType.QUEEN)))){
            return true;
        }
        danger_moves= Queen_Rook_bishop_move(pos,board,bishopdirections);
        if(king_helper(board,danger_moves,new ArrayList<>(Arrays.asList(PieceType.BISHOP,PieceType.QUEEN)))){
            return true;
        }
        danger_moves = knight_king_moves(pos,board,knightdirections);
        if(king_helper(board,danger_moves,new ArrayList<>(Arrays.asList(PieceType.KNIGHT)))){
            return true;
        }
        //still need to implement pawn danger
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        int[] attacks = {-1,2};
        int advance;
        ChessGame.TeamColor enemy_color;
        if(pieceColor == ChessGame.TeamColor.WHITE){
            advance = 1;
            enemy_color= ChessGame.TeamColor.BLACK;
        }
        else{
            advance = -1;
            enemy_color= ChessGame.TeamColor.WHITE;
        }
        moverrow+=advance;
        for(int attack: attacks){

            movercol+=attack;
            if(movercol>=1&&movercol<=8&&moverrow>=1&&moverrow<=8){
                ChessPosition attack_pos = new ChessPosition(moverrow,movercol);
//                if(movercol)
                ChessPiece piece = board.getPiece(attack_pos);
                if(board.getColor(attack_pos)==enemy_color&&piece.getPieceType()==PieceType.PAWN){
                    return true;
                }
            }
        }
        //queen and rookcheck;

        return false;
    }
    private Collection<ChessMove> Queen_Rook_bishop_move(ChessPosition pos, ChessBoard board, int[][] directions){
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor square_color = null;
        for (int[] direction:directions){
            while(moverrow>=1&&moverrow<=8&&movercol>=1&&movercol<=8&&square_color==null){
                moverrow += direction[0];
                movercol += direction[1];
                if(moverrow>0&&moverrow<9&&movercol>0&&movercol<9) {
                    ChessPosition new_pos = new ChessPosition(moverrow, movercol);
                    square_color = board.getColor(new_pos);
                    if(square_color!=pieceColor){
                        moves.add(new ChessMove(pos,new_pos,null));
                    }
                }
            }
            moverrow =row;
            movercol = col;
            square_color = null;
        }
        return moves;
    }
    private Collection<ChessMove> knight_king_moves(ChessPosition pos, ChessBoard board, int[][] directions){
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor square_color = null;
        for (int[] direction:directions){

            moverrow += direction[0];
            movercol += direction[1];
            if(moverrow>0&&moverrow<9&&movercol>0&&movercol<9) {
                ChessPosition new_pos = new ChessPosition(moverrow, movercol);
                square_color = board.getColor(new_pos);
                if(square_color!=pieceColor){
                    moves.add(new ChessMove(pos,new_pos,null));
                }

            }
            moverrow =row;
            movercol = col;
            square_color = null;
        }
        return moves;
    }
    private Collection<ChessMove> pawn_moves(ChessPosition pos, ChessBoard board){
        ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
        int row = pos.getRow();
        int moverrow = pos.getRow();
        int col = pos.getColumn();
        int movercol = pos.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
//        ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
        int[] attacks = {-1,2};
        int advance, promo,start;
        ChessGame.TeamColor enemy_color;
        if(pieceColor == ChessGame.TeamColor.WHITE){
            advance = 1;
            promo = 8;
            start = 2;
            enemy_color= ChessGame.TeamColor.BLACK;
        }
        else{
            advance = -1;
            promo = 1;
            start = 7;
            enemy_color= ChessGame.TeamColor.WHITE;
        }
        ChessGame.TeamColor square_color = null;
        ChessPosition new_pos = new ChessPosition(row+advance,col);
        if(board.getColor(new_pos) == null){
            if( new_pos.getRow()==promo){
                for (ChessPiece.PieceType promotion: promotions){
                    moves.add(new ChessMove(pos,new_pos,promotion));
                }
            }
            else {
                moves.add(new ChessMove(pos,new_pos,null));
                if(row==start){
                    ChessPosition double_advance = new ChessPosition(row+advance+advance,col);
                    if(board.getColor(double_advance)==null){
                        moves.add(new ChessMove(pos,double_advance,null));
                    }
                }
            }


        }
        moverrow+=advance;
        for(int attack: attacks){

            movercol+=attack;
            if(movercol>=1&&movercol<=8){
                ChessPosition attack_pos = new ChessPosition(moverrow,movercol);

                if(board.getColor(attack_pos)==enemy_color){
                    if(moverrow==promo){
                        for(ChessPiece.PieceType promotion: promotions){
                            moves.add(new ChessMove(pos,attack_pos,promotion));

                        }
                    }
                    else{
                        moves.add(new ChessMove(pos,attack_pos,null));

                    }
                }
            }
        }
        return moves;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = List.of();
        if(type == PieceType.KING){
            int[][] directions={{1,0},{1,-1},{0,-1}, {-1, -1},{-1,0},{-1,1},{0,1},{1,1}};
            return knight_king_moves(myPosition,board,directions);
        }
        if(type == PieceType.QUEEN){
            int[][] directions={{1,-1},{1,1},{-1,-1},{-1,1},{1,0},{0,1},{-1,0},{0,-1}};
            return Queen_Rook_bishop_move(myPosition,board,directions);
        }
        if(type == PieceType.KNIGHT){
            int[][] directions={{2,-1},{1,-2},{-1,-2}, {-2, -1},{-2,1},{-1,2},{1,2},{2,1}};
            return knight_king_moves(myPosition,board,directions);
        }
        if(type == PieceType.ROOK){
            int[][] directions={{1,0},{0,1},{-1,0},{0,-1}};
            return Queen_Rook_bishop_move(myPosition,board,directions);
        }
        if(type == PieceType.BISHOP){
            int[][] directions={{1,-1},{1,1},{-1,-1},{-1,1}};
            return Queen_Rook_bishop_move(myPosition,board,directions);

        }
        if(type == PieceType.PAWN){
            return pawn_moves(myPosition,board);
        }
        return moves;
    }
}


