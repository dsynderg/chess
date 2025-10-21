package Services;

import dataaccess.AuthDatabase;
import dataaccess.DatabaseRegistry;
import dataaccess.GameDatabase;
import dataaccess.UserDatabase;

public class DeleteService {
    static UserDatabase userdatabase = DatabaseRegistry.getUserDB();
    static AuthDatabase authdatabase = DatabaseRegistry.getAuthDB();
    static GameDatabase gamedatabase = DatabaseRegistry.getGameDB();

    public static boolean deleteAll() {
        return userdatabase.deleteall() && authdatabase.deleteall() && gamedatabase.deleteall();
    }
}
