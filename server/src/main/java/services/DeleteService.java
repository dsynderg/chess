package services;

import dataaccess.MemAuthDatabase;
import dataaccess.MemDatabaseRegistry;
import dataaccess.MemGameDatabase;
import dataaccess.MemUserDatabase;

public class DeleteService {
    static MemUserDatabase userdatabase = MemDatabaseRegistry.getUserDb();
    static MemAuthDatabase authdatabase = MemDatabaseRegistry.getAuthDb();
    static MemGameDatabase gamedatabase = MemDatabaseRegistry.getGameDb();

    public static boolean deleteAll() {
        return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall();
    }
}
