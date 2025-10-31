package dataaccess.memoryImplementaiton;

import modules.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemAuthDatabase implements MemDataBaseAccessor<AuthData> {
    ArrayList<AuthData> database = new ArrayList<>();

    public String getUsername(String authToken) {
        for (AuthData data : database) {
            if (Objects.equals(data.authToken(), authToken)) {
                return data.username();
            }
        }
        return null;
    }

    public ArrayList<AuthData> getDatabase() {
        return (ArrayList<AuthData>) database.clone();
    }

    public boolean inDatabase(String authToken) {
        for (AuthData authData : database) {
            if (Objects.equals(authData.authToken(), authToken)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addToDatabase(AuthData addObject) {
        if (inDatabase(addObject.authToken())) {
            return false;
        }
        database.add(addObject);
        return true;
    }

    @Override
    public boolean removeFromDatabase(AuthData removeObject) {
        if (!inDatabase(removeObject.authToken())) {
            return false;
        }
        database.remove(removeObject);
        return true;
    }

    @Override
    public boolean deleteall() {
        database.clear();
        return database.size() <= 0;
    }

}
