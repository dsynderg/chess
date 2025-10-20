package Services;

import chess.ChessGame;
import dataaccess.DatabaseRegistry;
import dataaccess.GameDatabase;
import modules.AuthData;
import modules.GameData;

import java.util.UUID;

public class GameService {
    GameDatabase gameDatabase = DatabaseRegistry.getGameDB();
    public boolean addGamedata(GameData data){
        return gameDatabase.addToDatabase(data);
    }
    public boolean checkGameName(String name){
        return (gameDatabase.inDatabase(name)!=null);
    }
    public GameData gameDataGenorator(String gameName){
        UUID uuid = UUID.randomUUID();
        String gameid = uuid.toString();
        GameData gameData = new GameData(gameid,null,null,gameName,new ChessGame());
        gameDatabase.addToDatabase(gameData);
        return gameData;
    }
}
