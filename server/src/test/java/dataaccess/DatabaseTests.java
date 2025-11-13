package dataaccess;

import chess.ChessGame;
import modules.AuthData;
import modules.GameData;
import modules.User;
import org.junit.jupiter.api.Test;
import services.AccountService;
import services.DeleteService;
import services.GameService;

import java.sql.SQLException;
import java.util.Objects;

public class DatabaseTests {
    @Test
    void deleteDatabase() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        assert SQLDeleteDataBase.deleteAll();
    }

    @Test
    void addToAuth() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("123", "myname is jeff");
        assert SQLAuthDatabase.addToDatabase(auth);
    }
    @Test
    void addAuththatAlreadyExists() throws DataAccessException {
        SQLTableControler.initialize();

        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("123", "myname is jeff");
        assert SQLAuthDatabase.addToDatabase(auth);
        assert !SQLAuthDatabase.addToDatabase(auth);
    }

    @Test
    void deleteAllAuth() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        assert SQLAuthDatabase.deleteall();
    }
    @Test
    void deleteAllUser() throws DataAccessException{
        SQLTableControler.initialize();
        assert SQLUserDatabase.deleteall();
    }
    @Test
    void deleteAllGame() throws DataAccessException{
        SQLTableControler.initialize();
        assert SQLGameDatabase.deleteAll();
    }
    @Test
    void usernamesPasswordMatch() throws DataAccessException {
        AccountService service = new AccountService();
        SQLDeleteDataBase.deleteAll();
        User user1 = new User("asdf","fjl","a;dslfkj");
        assert service.creatAccont(user1);
        assert SQLUserDatabase.passwordUsernameMatch(user1.password(), user1.username());

    }
    @Test
    void usernamesPasswordDontMatch() throws DataAccessException {
        AccountService service = new AccountService();
        SQLDeleteDataBase.deleteAll();
        User user1 = new User("asdf","fjl","a;dslfkj");
        assert service.creatAccont(user1);
        assert !SQLUserDatabase.passwordUsernameMatch(user1.password()," user1.username()");

    }
    @Test
    void checkusername() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("abc", "dexter");
        assert SQLAuthDatabase.addToDatabase(auth);
        String username = SQLAuthDatabase.getUsername(auth.authToken());
        assert Objects.equals(username, auth.username());
    }

    @Test
    void printDatabase() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("1", "dexter");
        assert SQLAuthDatabase.addToDatabase(auth);
        auth = new AuthData("2", "bob");
        assert SQLAuthDatabase.addToDatabase(auth);
        auth = new AuthData("3", "joe");
        assert SQLAuthDatabase.addToDatabase(auth);
        SQLAuthDatabase.listDatabase();

    }

    @Test
    void addToGame() throws SQLException, DataAccessException {
        SQLTableControler.initialize();
        assert DeleteService.deleteAll();

        GameData game = new GameData(1, "asdf", "asdf", "asdf", new ChessGame());
        assert SQLGameDatabase.deleteAll();
        game = SQLGameDatabase.addToDatabase(game);
        GameData gameCheck = SQLGameDatabase.inDatabaseID(game.gameID());
        assert gameCheck.gameID() == game.gameID();
    }
    @Test
    void joinGame() throws DataAccessException, SQLException {
        assert DeleteService.deleteAll();
        GameService service = new GameService();
        User user1 = new User("asdf","fjl","a;dslfkj");
        GameData game = new GameData(2136,"bob",null,"best game",new ChessGame());
        game = SQLGameDatabase.addToDatabase(game);
        assert !service.assignColor(user1.username(), ChessGame.TeamColor.WHITE,game.gameID());
    }


    @Test
    void listGamesWorks() throws SQLException, DataAccessException {
        SQLTableControler.initialize();
        assert SQLGameDatabase.deleteAll();
        GameData game1 = new GameData(1, "asdf", "asdfa", "asdfd", new ChessGame());
        GameData game2 = new GameData(2, "asdfb", "asdfs", "asdfs", new ChessGame());
        GameData game3 = new GameData(3, "asdfc", "asdfs", "asdfa", new ChessGame());
        game1 = SQLGameDatabase.addToDatabase(game1);
        game2 = SQLGameDatabase.addToDatabase(game2);
        game3 = SQLGameDatabase.addToDatabase(game3);
        SQLGameDatabase.listDatabase();
    }
    @Test
    void listEmptyGames() throws DataAccessException{
        SQLTableControler.initialize();
        SQLDeleteDataBase.deleteAll();
        SQLGameDatabase.listDatabase();
    }

    @Test
    void removeGame() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        assert SQLGameDatabase.deleteAll();
        GameData game1 = new GameData(145, "asdf", "asdfa", "asdfd", new ChessGame());
        game1 = SQLGameDatabase.addToDatabase(game1);
        assert SQLGameDatabase.inDatabase(game1.gameName());
        assert (SQLGameDatabase.inDatabaseID(game1.gameID()) != null);
        assert SQLGameDatabase.removeFromDatabase(game1);
        assert !SQLGameDatabase.inDatabase(game1.gameName());
    }
    @Test
    void removeGameThatDosntExist() throws DataAccessException{
        SQLTableControler.initialize();
        SQLDeleteDataBase.deleteAll();
        GameData game = new GameData(1,"sdf","asd","fjs",new ChessGame());
        assert !SQLGameDatabase.removeFromDatabase(game);
    }
    @Test
    void addUser() throws SQLException, DataAccessException {
        SQLTableControler.initialize();

        User user1 = new User("jake","abcdefg","afjdjk@gokdsd");
        assert !SQLUserDatabase.inDatabase(user1.username());
        assert SQLUserDatabase.addToDatabase(user1);
        assert SQLUserDatabase.inDatabase(user1.username());
    }
    @Test
    void addUserTwice() throws DataAccessException{
        SQLDeleteDataBase.deleteAll();
        User user1 = new User("dodf","aascdefg","afjdjk@gokdsd");
        assert !SQLUserDatabase.inDatabase(user1.username());
        assert SQLUserDatabase.addToDatabase(user1);
        assert !SQLUserDatabase.addToDatabase(user1);
    }
    @Test
    void removeUserThatDosntExist() throws DataAccessException{
        SQLTableControler.initialize();
        User user = new User("bob","password","asldfjksld");
        assert !SQLUserDatabase.removeFromDatabase(user);
    }
}