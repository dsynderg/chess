package dataaccess;

import modules.User;

import java.util.ArrayList;
import java.util.Map;

public class UserDatabase implements dataBaseAccessor<User>{
    ArrayList<User> database = new ArrayList<>();
    @Override
    public boolean inDatabase(String username) {
        for(User user:database){
            if(user.username()==username){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFromDatabase(User removeObject) {
        if(!inDatabase(removeObject.username())){
            return false;
        }
        database.remove(removeObject);
        return true;
    }


    @Override
    public boolean addToDatabase(User addObject) {
        if (inDatabase(addObject.username())){
            return false;
        }
        database.add(addObject);
        return true;
    }
}
