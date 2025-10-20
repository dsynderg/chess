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
    public GameData inDatabase(String gameName) {
        for(GameData data: database){
            if(Objects.equals(data.GameName(), gameName)){
                return data;
            }
        }
        return null;
    }

    @Override
    public boolean addToDatabase(GameData addObject) {
        if(inDatabase(addObject.GameName())!=null){
            return false;
        }
        database.add(addObject);
        return true;
    }

    @Override
    public boolean removeFromDatabase(GameData removeObject) {
        if(inDatabase(removeObject.GameName())==null){
            return false;
        }
        database.remove(removeObject);
        return true;
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
