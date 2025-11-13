package services;

import chess.ChessBoard;
import chess.ChessGame;
import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;

import java.util.*;

public class LoginService {

    private HttpHelper httpHelper = new HttpHelper();
    Gson gson = new Gson();

    private void help() {
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("LOGOUT: Logs you out");
        System.out.println("CREATE GAME: Creates a new game");
        System.out.println("LIST GAMES: Prints out all of the joinable games");
        System.out.println("JOIN GAME: joins a game");
        System.out.println("OBSERVE GAME: allows you to oberve a game");
        System.out.println("QUIT: Ends your session");
    }

    private void quit() {
        System.exit(1);
    }




    public void isLoggedin(AuthData authData) throws Exception {
        assert authData != null;
        //will eventually return a user object
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("LOGGED_IN>>>");
            String line = scanner.nextLine().trim().toLowerCase();
            if (Objects.equals(line, "help")) {
                help();
            }
            if (Objects.equals(line, "quit")) {
                quit();
            }
            if (Objects.equals(line, "logout")) {
                var response = httpHelper.requestMaker(RequestType.delete, "session", gson.toJson(authData), authData);
                if (response.statusCode() == 200) {
                    return;
                }
//                throw new AssertionError();
            }
            if (Objects.equals(line, "create game")) {
                System.out.print("What is the game name? ");
                String gamename = scanner.nextLine().trim().toLowerCase();
                String json = "{ \"gameName\":\""+gamename+"\" }";
                var response = httpHelper.requestMaker(RequestType.post, "game", json, authData);



            }
            if(Objects.equals(line,"list games")){
                httpHelper.requestMaker(RequestType.get,"game",null,authData);

            }
            if(Objects.equals(line,"join game")){
                System.out.print("What game would you like to join? ");
                String gameID = scanner.nextLine().trim().toLowerCase();
                System.out.print("Which color would you like to be? ");
                String gameColor = scanner.nextLine().trim().toLowerCase();
                String json = "{ \"playerColor\":\""+gameColor+"\", \"gameID\": "+gameID+" }";
                try {
                    var response = httpHelper.requestMaker(RequestType.put, "game", json, authData);
                    ChessGame.TeamColor side = (gameColor.equals("black")) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    BoardPrinter.printBoard(board, side);
                }
                catch (Exception e){
                    System.out.println("There was a problem joining the game, please check your inputs and try again");
                }
            }

            if(Objects.equals(line,"observe game")){
                System.out.print("Which game would you like to observe");
                String observedGame = scanner.nextLine().trim().toLowerCase();
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                BoardPrinter.printBoard(board, ChessGame.TeamColor.WHITE);

            }

        }

    }
}

