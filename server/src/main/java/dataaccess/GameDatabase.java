package dataaccess;

import modules.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameDatabase implements dataBaseAccessor<GameData> {
    ArrayList<GameData> database = new ArrayList<>();

    public ArrayList<GameData> getDatabase() {
        return (ArrayList<GameData>) database.clone();
    }

    public GameData inDatabaseName(String gameName) {
        for (GameData data : database) {
            if (Objects.equals(data.gameName(), gameName)) {
                return data;
            }
        }
        return null;
    }

    public GameData inDatabaseID(int gameName) {
        for (GameData data : database) {
            if (Objects.equals(data.gameID(), gameName)) {
                return data;
            }
        }
        return null;
    }

    @Override
    public boolean addToDatabase(GameData addObject) {
        if (inDatabaseName(addObject.gameName()) != null) {
            return false;
        }
        database.add(addObject);
        return true;
    }

    @Override
    public boolean removeFromDatabase(GameData removeObject) {
        if (inDatabaseName(removeObject.gameName()) == null) {
            return false;
        }
        database.remove(removeObject);
        return true;
    }

    @Override
    public boolean deleteall() {
        database.clear();
        if (!database.isEmpty()) {
            return false;
        }
        return true;
    }
}
