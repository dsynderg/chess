package dataaccess;

import modules.AuthData;
import modules.GameData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class GameDatabase implements dataBaseAccessor<GameData>  {
    ArrayList<GameData> database = new ArrayList<>();
    public ArrayList<GameData> getDatabase(){
        return (ArrayList<GameData>) database.clone();
    }
    public boolean inDatabase(String gameName) {
        for(GameData data: database){
            if(Objects.equals(data.GameName(), gameName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addToDatabase(GameData addObject) {
        return false;
    }

    @Override
    public boolean removeFromDatabase(GameData removeObject) {
        return false;
    }

    @Override
    public boolean deleteall() {
        database.clear();
        if(database.size()>0){
            return false;
        }
        return true;
    }
}
