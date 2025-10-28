package services;

import chess.ChessGame;
import dataaccess.MemDatabaseRegistry;
import dataaccess.MemGameDatabase;
import modules.GameData;

import java.util.ArrayList;

public class GameService {
    MemGameDatabase gameDatabase = MemDatabaseRegistry.getGameDb();
    private int gameID = 0;


    public boolean checkGameID(int gameID) {
        return (gameDatabase.inDatabaseID(gameID) != null);
    }


    public GameData gameDataGenorator(String gameName) {
        if(gameName == ""){
            return null;
        }
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

