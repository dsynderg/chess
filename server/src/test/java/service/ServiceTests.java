package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import modules.AuthData;
import modules.GameData;
import services.AccountService;
import services.DatabaseConfigLoader;
import services.DeleteService;
import modules.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GameService;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceTests {
    AccountService aS = new AccountService();
    GameService gS = new GameService();
    boolean isMemory;

    {
        try {
            isMemory = DatabaseConfigLoader.memoryimplementation();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("clear database")
    void clearAll() throws SQLException, DataAccessException {
        User user1 = new User("josdfse", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert !aS.checkUsername(user1.username());
    }
    @Test
    void clearAllFail() throws SQLException, DataAccessException {
        User user1 = new User("joe", ";alsdkfj", "sdfl@sdf");
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
        assert DeleteService.deleteAll();
        assert aS.creatAccont(user1);

        assert aS.checkUsername(user1.username());
    }
    @Test
    void failRegister() throws SQLException, DataAccessException {
        User user1 = new User(null,null,"asdfk@gsdlf");
        assert !aS.creatAccont(user1);
    }
    @Test
    void register() throws SQLException, DataAccessException {
        User user1 = new User("sldkf","asldkj","jflasjdf");
        assert !aS.checkUsername(user1.username());
        assert aS.creatAccont(user1);
        assert aS.checkUsername(user1.username());
    }

    @Test
    void repeteAccounts() throws SQLException, DataAccessException {
        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        assert !aS.creatAccont(dummyUser);
    }
    @Test
    void loginTest() throws SQLException, DataAccessException {
        DeleteService.deleteAll();
        User dummyUser = new User("asdf", "a;sdfl", "sldjflsdj");
        assert !aS.checkUsername(dummyUser.username());
        assert aS.creatAccont(dummyUser);
        AuthData auth = aS.authDataGenorator(dummyUser.username());
        assert aS.checkAuth(auth.authToken());
    }
    @Test
    void loginWithInvalidAuth() throws SQLException, DataAccessException {
        assert !aS.checkAuth("abcdefg");
    }
    @Test
    void logout() throws SQLException, DataAccessException {
        User logoutuser = new User("logout", "fdlfkj", "a;sdlfkj@gdsf");
        assert aS.creatAccont(logoutuser);
        modules.AuthData authToken = aS.authDataGenorator(logoutuser.username());
        assert aS.checkAuth(authToken.authToken());
        assert aS.removeAuth(authToken.authToken());
        assert !aS.checkAuth(authToken.authToken());
    }
    @Test
    void logoutfail() throws SQLException, DataAccessException {
        assert !aS.removeAuth("this sucks");
    }
    @Test
    void listgamesSuccess() throws SQLException, DataAccessException {
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
    void listGamesFail() throws SQLException, DataAccessException {
        assert !aS.checkAuth("lsjdfsldjf");
    }
    @Test
    void creatGame() throws SQLException, DataAccessException {
        modules.GameData game = gS.gameDataGenorator("testgame");
        assert gS.checkGameID(game.gameID());
    }
    @Test
    void creatGameFail() throws SQLException, DataAccessException {
        assert gS.gameDataGenorator("")==null;
    }
}
