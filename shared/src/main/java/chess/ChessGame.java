package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turnColor = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turnColor == chessGame.turnColor && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnColor, board);
    }

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return turnColor;
    }

    /**
     * sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        turnColor = team;
    }
    public void switchTurn(){
        //if the color is white then it switches to black
        if (turnColor == TeamColor.WHITE){
           setTeamTurn(TeamColor.BLACK);
        }
        //if the color is black it switches to white
        else {
            setTeamTurn(TeamColor.WHITE);
        }
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition){

        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessMove> return_moves = new ArrayList<>();

        ChessBoard tempboard = board.clone();
        if (piece != null){
                moves = piece.pieceMoves(board,startPosition);
            }
        for(ChessMove move:moves){
            updateBoardState(move);
            if(!isInCheck(getTeamTurn())){
                return_moves.add(move);
            }
            board = tempboard;
        }
        return return_moves;
    }
    public Collection<ChessMove> allmoves(ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i =1; i<9; i++){
            for (int j = 1; j<9; j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece temp_piece = board.getPiece(pos);
                if(board.getColor(pos)==teamColor){
                    moves.addAll(validMoves(pos));
                }
            }

        }

        return moves;
    }


    public void updateBoardState(ChessMove move){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);
        board.addPiece(start,null);
        board.addPiece(end,movingPiece);
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if(validMoves(start).contains(end)){
            updateBoardState(move);
            switchTurn();
        }
        else {
            throw new InvalidMoveException(move.toString());
        }
    }

    /**
     * Determines if the given team is in check
     * This should be changed if the movement of a pice casues a king to be in check
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
      ChessPosition king_pos = board.getKingPosition(teamColor);
      ChessPiece king = board.getPiece(king_pos);
      return king.isKingInCheck(board,king_pos);

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)  {
//        throw new RuntimeException("Not implemented");
        Collection<ChessMove> allmoves = allmoves(teamColor);
        boolean kingincheck = isInCheck(teamColor);
        if (isInCheck(teamColor) && allmoves(teamColor).isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)  {

//        throw new RuntimeException("Not implemented");
            if (!isInCheck(teamColor) && allmoves(teamColor).isEmpty()){
                return true;
            }
            return false;
        }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
    this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
