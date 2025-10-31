package services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDatabase;
import dataaccess.memoryImplementaiton.MemDatabaseRegistry;
import dataaccess.memoryImplementaiton.MemGameDatabase;
import modules.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameService {
    MemGameDatabase gameDatabase = MemDatabaseRegistry.getGameDb();
    private int gameID = 0;
    private boolean isMemoryImplemntation = DatabaseConfigLoader.memoryimplementation();

    public boolean checkGameID(int gameID) throws SQLException, DataAccessException {
        if(isMemoryImplemntation) {
            return (gameDatabase.inDatabaseID(gameID) != null);
        }
        else{
            return (SQLGameDatabase.inDatabaseID(gameID)!= null);
        }

    }


    public GameData gameDataGenorator(String gameName) throws SQLException, DataAccessException {
        if(gameName == ""){
            return null;
        }
        gameID++;
        int gameid = gameID;
        GameData gameData = new GameData(gameid, null, null, gameName, new ChessGame());
        if(isMemoryImplemntation) {

            if (gameDatabase.addToDatabase(gameData)) {
                return gameData;
            }

        }
        else{
            if(SQLGameDatabase.addToDatabase(gameData)){
                return gameData;
            }
        }
        return null;
    }

    public ArrayList<GameData> getGames() throws SQLException, DataAccessException {
        if(isMemoryImplemntation){
        return gameDatabase.getDatabase();
    }
        else {
            return SQLGameDatabase.listDatabase();
        }

    }

    public boolean assignColor(String username, ChessGame.TeamColor joinColor, int gameID) throws SQLException, DataAccessException {
        GameData data;
        if(isMemoryImplemntation){
                data = gameDatabase.inDatabaseID(gameID);
            }
            else{
                data = SQLGameDatabase.inDatabaseID(gameID);
            }
            assert data != null;


            if (joinColor == ChessGame.TeamColor.WHITE) {
                if (data.whiteUsername() == null) {
                    GameData updatedData = new GameData(
                            data.gameID(),
                            username,               // new whiteUsername
                            data.blackUsername(),
                            data.gameName(),
                            data.game()
                    );
                    if(isMemoryImplemntation) {
                        gameDatabase.removeFromDatabase(data);
                        gameDatabase.addToDatabase(updatedData);
                    }
                    else {
                        SQLGameDatabase.removeFromDatabase(data);
                        SQLGameDatabase.addToDatabase(updatedData);
                    }
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
                    if(isMemoryImplemntation) {
                        gameDatabase.removeFromDatabase(data);
                        gameDatabase.addToDatabase(updatedData);
                    }
                    else{
                        SQLGameDatabase.removeFromDatabase(data);
                        SQLGameDatabase.addToDatabase(updatedData);
                    }
                    return true;

                }
            }
            return false;


    }
}

