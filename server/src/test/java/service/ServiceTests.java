package service;

import chess.ChessGame;
import modules.AuthData;
import modules.GameData;
import services.AccountService;
import services.DatabaseConfigLoader;
import services.DeleteService;
import modules.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GameService;

import java.util.ArrayList;

public class ServiceTests {
    AccountService aS = new AccountService();
    GameService gS = new GameService();
    boolean isMemory = DatabaseConfigLoader.memoryimplementation();

    @Test
    @DisplayName("clear database")
    void clearAll() {
        User user1 = new User("josdfse", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert !aS.checkUsername(user1.username());
    }
    @Test
    void clearAllFail(){
        User user1 = new User("joe", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert aS.creatAccont(user1);

        assert aS.checkUsername(user1.username());
    }
    @Test
    void failRegister(){
        User user1 = new User(null,null,"asdfk@gsdlf");
        assert !aS.creatAccont(user1);
    }
    @Test
    void register(){
        User user1 = new User("sldkf","asldkj","jflasjdf");
        assert !aS.checkUsername(user1.username());
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
    }

    @Test
    void repeteAccounts() {
        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        assert !aS.creatAccont(dummyUser);
    }
    @Test
    void loginTest(){

        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        AuthData auth = aS.authDataGenorator(dummyUser.username());
        assert aS.checkAuth(auth.authToken());
    }
    @Test
    void loginWithInvalidAuth(){
        assert !aS.checkAuth("abcdefg");
    }
    @Test
    void logout(){
        User logoutuser = new User("logout", "fdlfkj", "a;sdlfkj@gdsf");
        assert aS.creatAccont(logoutuser);
        modules.AuthData authToken = aS.authDataGenorator(logoutuser.username());
        assert aS.checkAuth(authToken.authToken());
        assert aS.removeAuth(authToken.authToken());
        assert !aS.checkAuth(authToken.authToken());
    }
    @Test
    void logoutfail(){
        assert !aS.removeAuth("this sucks");
    }
    @Test
    void listgamesSuccess(){
        DeleteService.deleteAll();
        for(int i = 0; i<3;i++){
            GameData game = gS.gameDataGenorator(String.valueOf(i));
        }
        ArrayList<GameData> games = gS.getGames();
//        if(isMemory) {
            assert games != null;
            for (GameData game : games) {
                System.out.println(game);
            }
//        }
    }
    @Test
    void listGamesFail(){
        assert !aS.checkAuth("lsjdfsldjf");
    }
    @Test
    void creatGame(){
        modules.GameData game = gS.gameDataGenorator("testgame");
        assert gS.checkGameID(game.gameID());
    }
    @Test
    void creatGameFail(){
        assert gS.gameDataGenorator("")==null;
    }
}
