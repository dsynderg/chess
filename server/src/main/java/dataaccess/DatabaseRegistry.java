package dataaccess;

public class DatabaseRegistry {
    private static final AuthDatabase AuthDB = new AuthDatabase();
    private static final UserDatabase UserDB = new UserDatabase();
    private static final GameDatabase GameDB = new GameDatabase();

    public static AuthDatabase getAuthDB() {
        return AuthDB;
    }

    public static UserDatabase getUserDB() {
        return UserDB;

    }

    public static GameDatabase getGameDB() {
        return GameDB;
    }
}
