package services;

import clientenums.RequestType;
import com.google.gson.Gson;
import modules.AuthData;
import modules.User;

import java.util.*;

public class LoginService {

    private HttpHelper httpHelper = new HttpHelper();

    private void help() {
        System.out.println("HELP: Gets list of relevant commands");
        System.out.println("LOGOUT: Logs you out");
        System.out.println("CREATE GAME: Creates a new game");
        System.out.println("LIST GAMES: Prints out all of the joinable games");
        System.out.println("PLAY GAME: joins a game");
        System.out.println("OBSERVE GAME: allows you to oberve a game");
        System.out.println("QUIT: Ends your session");
    }

    private void createGame(String name){

    }
    private void listGames(){

    }
    private void playGame(String id){

    }
    private void observe(String id){
        
    }
    private void quit() {
        System.exit(1);
    }




    public void isLoggedin(AuthData authData) {
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
            if( Objects.equals(line,"logout")){
                return;
            }
        }

    }
}

