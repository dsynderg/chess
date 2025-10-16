package dataaccess;

import modules.User;

import java.util.ArrayList;
import java.util.Map;

public interface dataBaseAccessor<idStorageType> {
    ArrayList<idStorageType> database = new ArrayList<>();

    boolean inDatabase(String searchKey);
    boolean addToDatabase(idStorageType addObject);
    boolean removeFromDatabase(idStorageType removeObject);
}
