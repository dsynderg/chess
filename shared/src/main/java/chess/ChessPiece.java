package chess;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public Collection<ChessMove> knights_move(ChessPosition pos, ChessBoard board){
        List<ChessMove> knightMoveList = new ArrayList<>();
        //offset is used to cycle left and right, up and down, to make the L shape

        int row = pos.getRow();
        int col = pos.getColumn();
        ChessGame.TeamColor square_piece_color = null;
        int[] offset = {-1,1};
        int[] onset = {2,-2};
        for (int off : offset){
            for (int on : onset){
                ChessPosition endpos = new ChessPosition(row+on,col+off);
                square_piece_color = board.getColor(endpos);
                if (pieceColor!=square_piece_color) {
                    knightMoveList.add(new ChessMove(pos, endpos, null));
                }
                square_piece_color=null;
            }
        }
        for (int off : offset){
            for (int on : onset){
                ChessPosition endpos = new ChessPosition(row+off,col+on);
                square_piece_color = board.getColor(endpos);
                if (pieceColor!=square_piece_color) {
                    knightMoveList.add(new ChessMove(pos, endpos, null));
                }
                square_piece_color = null;
            }
        }
        return knightMoveList;
    }
    public Collection<ChessMove> lat_vert(ChessPosition pos, ChessBoard board){
        //directions 0 is north 1 is east 2 is west and 3 is south
        //This could also be used in some way to test king danger
        List<ChessMove> rookMoveList = new ArrayList<>();


        int row = pos.getRow();
        int moverRow = row;
        int col = pos.getColumn();
        int moverCol = col;
        ChessGame.TeamColor square_piece_color = null;
        //make a while loop, whil the position isn't negative && board position doesn't have a pice on it
        //while right
        while (moverCol<8 && square_piece_color==null){
            moverCol++;
            ChessPosition position = new ChessPosition(row,moverCol);
            if(board.getPiece(position)!=null){
                square_piece_color = board.getColor(position);
            }

            if (square_piece_color != pieceColor){
                rookMoveList.add(new ChessMove(pos,new ChessPosition(row,moverCol),null));
            }
        }
        moverCol = col;
        square_piece_color = null;
        //while left
        while (moverCol>1 && square_piece_color==null){
            moverCol--;
            ChessPosition position = new ChessPosition(row,moverCol);
            if(board.getPiece(position)!=null){
                square_piece_color = board.getColor(position);
            }

            if (square_piece_color != pieceColor){
                rookMoveList.add(new ChessMove(pos,new ChessPosition(row,moverCol),null));
            }
        }
        //while up
        while (moverRow<8 && square_piece_color==null){
            moverRow++;
            ChessPosition position = new ChessPosition(moverRow,col);
            if(board.getPiece(position)!=null){
                square_piece_color = board.getColor(position);
            }

            if (square_piece_color != pieceColor){
                rookMoveList.add(new ChessMove(pos,new ChessPosition(moverRow,col),null));
            }
        }
        moverRow = row;
        square_piece_color = null;
        //while down
        while (moverRow>1 && square_piece_color==null){
            moverRow--;
            ChessPosition position = new ChessPosition(moverRow,col);
            if(board.getPiece(position)!=null){
                square_piece_color = board.getColor(position);
            }

            if (square_piece_color != pieceColor){
                rookMoveList.add(new ChessMove(pos,new ChessPosition(moverRow,col),null));
            }
        }
        return rookMoveList;
    }

    public Collection<ChessMove> bishops_move(ChessPosition pos, ChessBoard board){
        List<ChessMove> moves = new ArrayList<>();
        int[] northwest = {1,-1};
        int[] northeast = {1,1};
        int[] southwest = {-1,-1};
        int[] southeast = {-1,1};
        int[][] directions = new int[][]{northwest, northeast, southwest, southeast};
        int row = pos.getRow();
        int moverRow = row;
        int col = pos.getColumn();
        int moverCol = col;
        ChessGame.TeamColor square_piece_color=null;
        for(int[] direction:directions){
            int up = direction[0];
            int right = direction[1];
            while(moverRow<8 && moverRow>1 && moverCol<8 && moverCol>1
                    && square_piece_color==null){
                moverRow+=up;
                moverCol+=right;
                ChessPosition new_position = new ChessPosition(moverRow,moverCol);
                if(board.getPiece(new_position)!=null){
                    square_piece_color = board.getColor((new_position));
                }
                if (square_piece_color != pieceColor){
                    moves.add(new ChessMove(pos,new_position,null));
                }
            }
            moverRow = row;
            moverCol = col;
            square_piece_color = null;

        }


        return moves;
    }
    public Collection<ChessMove> queens_move(ChessPosition pos,ChessBoard board){
        Collection<ChessMove> latteral_moves = lat_vert(pos,board);
        Collection<ChessMove> diagonal_moves = bishops_move(pos,board);
        latteral_moves.addAll(diagonal_moves);
        return latteral_moves;

    }
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
//    public Collection<ChessMove> pawns_move(ChessPosition pos){
////        chess.ChessPosition position = chess.ChessMove.getStartPosition();
//
//    }
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


