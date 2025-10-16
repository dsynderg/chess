package dataaccess;

import modules.AuthData;

import java.util.ArrayList;

public class AuthDatabase implements dataBaseAccessor<AuthData>{
    ArrayList<AuthData> database = new ArrayList<>();

    public boolean inDatabase(String searchKey) {
        for(AuthData authData:database){
            if (authData.authToken()==searchKey){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addToDatabase(AuthData addObject) {
        if(inDatabase(addObject.authToken())){
            return false;
        }
        database.add(addObject);
        return true;
    }

    @Override
    public boolean removeFromDatabase(AuthData removeObject) {
        if(!inDatabase(removeObject.authToken())){
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
