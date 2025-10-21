package services;

import dataaccess.AuthDatabase;
import dataaccess.DatabaseRegistry;
import dataaccess.GameDatabase;
import dataaccess.UserDatabase;

public class DeleteService {
    static UserDatabase userdatabase = DatabaseRegistry.getUserDb();
    static AuthDatabase authdatabase = DatabaseRegistry.getAuthDb();
    static GameDatabase gamedatabase = DatabaseRegistry.getGameDb();

    public static boolean deleteAll() {
        return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall();
    }
}
