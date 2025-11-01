package services;
import dataaccess.DataAccessException;
import dataaccess.SQLTableControler;

//allows you to ch
public class DatabaseConfigLoader {
    private static boolean mI = true;
    public static boolean memoryimplementation() throws DataAccessException {
        if(!mI){
            try {
                SQLTableControler.initialize();
            } catch (DataAccessException e) {
                throw new DataAccessException("database problem",e);
            }
        }

        return mI;


    }
}
