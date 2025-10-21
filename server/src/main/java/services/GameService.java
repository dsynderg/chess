package services;

import chess.ChessGame;
import dataaccess.DatabaseRegistry;
import dataaccess.GameDatabase;
import modules.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class GameService {
    GameDatabase gameDatabase = DatabaseRegistry.getGameDb();
    private int gameID = 0;

//    public boolean addGamedata(GameData data) {
//        return gameDatabase.addToDatabase(data);
//    }
//
//
//    public boolean checkGameName(String name) {
//        return (gameDatabase.inDatabaseName(name) != null);
//    }

    public boolean checkGameID(int gameID) {
        return (gameDatabase.inDatabaseID(gameID) != null);
    }


    public GameData gameDataGenorator(String gameName) {
        UUID uuid = UUID.randomUUID();
        gameID++;
        int gameid = gameID;
        GameData gameData = new GameData(gameid, null, null, gameName, new ChessGame());
        if (gameDatabase.addToDatabase(gameData)) {
            return gameData;
        }
        return null;

    }

    public ArrayList<GameData> getGames() {
        return gameDatabase.getDatabase();
    }

    public boolean assignColor(String username, ChessGame.TeamColor joinColor, int gameID) {
        GameData data = gameDatabase.inDatabaseID(gameID);
        if (joinColor == ChessGame.TeamColor.WHITE) {
            if (data.whiteUsername() == null) {
                GameData updatedData = new GameData(
                        data.gameID(),
                        username,               // new whiteUsername
                        data.blackUsername(),
                        data.gameName(),
                        data.game()
                );
                gameDatabase.removeFromDatabase(data);
                gameDatabase.addToDatabase(updatedData);
                return true;
            }
        }
        if (joinColor == ChessGame.TeamColor.BLACK) {
            if (data.blackUsername() == null) {
                GameData updatedData = new GameData(
                        data.gameID(),
                        data.whiteUsername(),               // new whiteUsername
                        username,
                        data.gameName(),
                        data.game()
                );
                gameDatabase.removeFromDatabase(data);
                gameDatabase.addToDatabase(updatedData);
                return true;

            }
        }
        return false;
    }
}

