package dataaccess;

import java.util.ArrayList;

public interface dataBaseAccessor<idStorageType> {
    ArrayList<Object> database = new ArrayList<>();

//    boolean inDatabase(Object searchObject);
    boolean addToDatabase(idStorageType addObject);
    boolean removeFromDatabase(idStorageType removeObject);
    boolean deleteall();
}
