package dataaccess.memoryimplementaiton;

import java.util.ArrayList;

public interface MemDataBaseAccessor<T> {
    ArrayList<Object> DATABASE = new ArrayList<>();

    //    boolean inDatabase(Object searchObject);
    boolean addToDatabase(T addObject);

    boolean removeFromDatabase(T removeObject);

    boolean deleteall();
}
