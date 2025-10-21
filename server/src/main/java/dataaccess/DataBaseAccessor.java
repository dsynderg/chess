package dataaccess;

import java.util.ArrayList;

public interface DataBaseAccessor<idStorageType> {
    ArrayList<Object> DATABASE = new ArrayList<>();

    //    boolean inDatabase(Object searchObject);
    boolean addToDatabase(idStorageType addObject);

    boolean removeFromDatabase(idStorageType removeObject);

    boolean deleteall();
}
