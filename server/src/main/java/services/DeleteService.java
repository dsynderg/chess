package services;

import dataaccess.*;

public class DeleteService {
    static MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    static MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    static MemGameDatabase gamedatabase = MemDatabaseRegistry.getGameDb();
    private static boolean isMemoryImplemntation = DatabaseConfigLoader.memoryimplementation();

    public static boolean deleteAll() {
        if(isMemoryImplemntation) {
            return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall();
        }
        else{
            SQLDeleteDataBase.deleteAll();
        }

        return false;
    }
}
