package services;

import dataaccess.*;
import dataaccess.memoryImplementaiton.MemAuthDatabase;
import dataaccess.memoryImplementaiton.MemDatabaseRegistry;
import dataaccess.memoryImplementaiton.MemGameDatabase;
import dataaccess.memoryImplementaiton.MemUserDatabase;

import java.sql.SQLException;

public class DeleteService {
    static MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    static MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    static MemGameDatabase gamedatabase = MemDatabaseRegistry.getGameDb();
    private static boolean isMemoryImplemntation = DatabaseConfigLoader.memoryimplementation();

    public static boolean deleteAll() throws SQLException, DataAccessException {
        if(isMemoryImplemntation) {
            return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall();
        }
        else{
            return SQLUserDatabase.deleteall() && SQLGameDatabase.deleteAll()&&SQLAuthDatabase.deleteall();
        }


    }
}
