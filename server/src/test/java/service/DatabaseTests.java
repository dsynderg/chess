package service;

import chess.ChessGame;
import dataaccess.SQLAuthDatabase;
import dataaccess.SQLDeleteDataBase;
import dataaccess.SQLGameDatabase;
import dataaccess.SQLUserDatabase;
import dataaccess.memoryImplementaiton.MemGameDatabase;
import modules.AuthData;
import modules.GameData;
import modules.User;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class DatabaseTests {
    @Test
    void deleteDatabase() {
        assert SQLDeleteDataBase.deleteAll();
    }

    @Test
    void addToAuth() {
        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("123", "myname is jeff");
        assert SQLAuthDatabase.addToDatabase(auth);
    }

    @Test
    void deleteAllAuth() {
        assert SQLAuthDatabase.deleteall();
    }

    @Test
    void checkusername() {
        SQLAuthDatabase.deleteall();
        AuthData auth = new AuthData("abc", "dexter");
        assert SQLAuthDatabase.addToDatabase(auth);
        String username = SQLAuthDatabase.getUsername(auth.authToken());
        assert Objects.equals(username, auth.username());
    }

    @Test
    void printDatabase() {
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
    void addToGame() {
        GameData game = new GameData(1, "asdf", "asdf", "asdf", new ChessGame());
        assert SQLGameDatabase.deleteAll();
        assert SQLGameDatabase.addToDatabase(game);
    }


    @Test
    void listGamesWorks() {
        assert SQLGameDatabase.deleteAll();
        GameData game1 = new GameData(1, "asdf", "asdfa", "asdfd", new ChessGame());
        GameData game2 = new GameData(2, "asdfb", "asdfs", "asdfs", new ChessGame());
        GameData game3 = new GameData(3, "asdfc", "asdfs", "asdfa", new ChessGame());
        assert SQLGameDatabase.addToDatabase(game1);
        assert SQLGameDatabase.addToDatabase(game2);
        assert SQLGameDatabase.addToDatabase(game3);
        SQLGameDatabase.listDatabase();
    }

    @Test
    void removeGame() {
        assert SQLGameDatabase.deleteAll();
        GameData game1 = new GameData(145, "asdf", "asdfa", "asdfd", new ChessGame());
        assert SQLGameDatabase.addToDatabase(game1);
        assert SQLGameDatabase.inDatabase(game1.gameName());
        assert (SQLGameDatabase.inDatabaseID(game1.gameID()) != null);
        assert SQLGameDatabase.removeFromDatabase(game1);
        assert !SQLGameDatabase.inDatabase(game1.gameName());
    }
    @Test
    void addUser() {
        User user1 = new User("jake","abcdefg","afjdjk@gokdsd");
        assert !SQLUserDatabase.inDatabase(user1.username());
        assert SQLUserDatabase.addToDatabase(user1);
        assert SQLUserDatabase.inDatabase(user1.username());
    }
}