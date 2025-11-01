package services;

import dataaccess.*;
import dataaccess.memoryimplementaiton.MemAuthDatabase;
import dataaccess.memoryimplementaiton.MemDatabaseRegistry;
import dataaccess.memoryimplementaiton.MemGameDatabase;
import dataaccess.memoryimplementaiton.MemUserDatabase;

import java.sql.SQLException;

public class DeleteService {
    static MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    static MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    static MemGameDatabase gamedatabase = MemDatabaseRegistry.getGameDb();
    private static boolean isMemoryImplemntation;

    static {
        try {
            isMemoryImplemntation = DatabaseConfigLoader.memoryimplementation();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteAll() throws SQLException, DataAccessException {

            return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall()&&SQLUserDatabase.deleteall()
                    && SQLGameDatabase.deleteAll()&&SQLAuthDatabase.deleteall();



    }
}
