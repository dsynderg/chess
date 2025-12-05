package services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDatabase;
import dataaccess.memoryimplementaiton.MemDatabaseRegistry;
import dataaccess.memoryimplementaiton.MemGameDatabase;
import modules.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    MemGameDatabase gameDatabase = MemDatabaseRegistry.getGameDb();
    private int gameID = 0;
    private boolean isMemoryImplemntation;

    {
        try {
            isMemoryImplemntation = DatabaseConfigLoader.memoryimplementation();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBoard(GameData updateData) throws DataAccessException {
        GameData gameDataToRemove = SQLGameDatabase.inDatabaseID(updateData.gameID());
        assert gameDataToRemove != null;
        SQLGameDatabase.removeFromDatabase(gameDataToRemove);
        SQLGameDatabase.updateDatabase(updateData);
    }
    public boolean checkGameID(int gameID) throws DataAccessException {
        if (isMemoryImplemntation) {
            return (gameDatabase.inDatabaseID(gameID) != null);
        } else {
            return (SQLGameDatabase.inDatabaseID(gameID) != null);
        }

    }


    public GameData gameDataGenorator(String gameName) throws DataAccessException {
        if (gameName == "") {
            return null;
        }
        gameID++;
        int gameid = gameID;


        GameData gameData = new GameData(gameid, null, null, gameName, new ChessGame());
        if (isMemoryImplemntation) {

            if (gameDatabase.addToDatabase(gameData)) {
                return gameData;
            }

        } else {
            gameData = SQLGameDatabase.addToDatabase(gameData);
            if (gameData != null) {
                return gameData;
            }
        }
        return null;
    }

    public ArrayList<GameData> getGames() throws DataAccessException {
        if (isMemoryImplemntation) {
            return gameDatabase.getDatabase();
        } else {
            return SQLGameDatabase.listDatabase();
        }

    }

    public boolean assignColor(String username, ChessGame.TeamColor joinColor, int gameID) throws DataAccessException {
        GameData data;
        if (isMemoryImplemntation) {
            data = gameDatabase.inDatabaseID(gameID);
        } else {
            data = SQLGameDatabase.inDatabaseID(gameID);
        }
        assert data != null;


        if (joinColor == ChessGame.TeamColor.WHITE) {
            if(Objects.equals(username, data.whiteUsername())){
                return true;
            }
            if (data.whiteUsername() == null ||username==null) {
                GameData updatedData = new GameData(
                        data.gameID(),
                        username,               // new whiteUsername
                        data.blackUsername(),
                        data.gameName(),
                        data.game()
                );
                if (isMemoryImplemntation) {
                    gameDatabase.removeFromDatabase(data);
                    gameDatabase.addToDatabase(updatedData);
                } else {
                    SQLGameDatabase.removeFromDatabase(data);
                    SQLGameDatabase.updateDatabase(updatedData);
                }
                return true;
            }
        }
        if (joinColor == ChessGame.TeamColor.BLACK) {
            if (Objects.equals(username, data.blackUsername())){
                return true;
            }
            if (data.blackUsername() == null||username==null) {
                GameData updatedData = new GameData(
                        data.gameID(),
                        data.whiteUsername(),               // new whiteUsername
                        username,
                        data.gameName(),
                        data.game()
                );
                if (isMemoryImplemntation) {
                    gameDatabase.removeFromDatabase(data);
                    gameDatabase.addToDatabase(updatedData);
                } else {
                    SQLGameDatabase.removeFromDatabase(data);
                    SQLGameDatabase.updateDatabase(updatedData);
                }
                return true;

            }

        }
        return false;
    }
    public GameData inDatabaseID(int gameID) throws DataAccessException {
        if (isMemoryImplemntation){
           return gameDatabase.inDatabaseID(gameID);
        }
        else {
            return SQLGameDatabase.inDatabaseID(gameID);
        }
    }
}


