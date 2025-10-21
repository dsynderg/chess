package dataaccess;

public class DatabaseRegistry {
    private static final AuthDatabase AUTH_DB = new AuthDatabase();
    private static final UserDatabase USER_DB = new UserDatabase();
    private static final GameDatabase GAME_DB = new GameDatabase();

    public static AuthDatabase getAuthDb() {
        return AUTH_DB;
    }

    public static UserDatabase getUserDb() {
        return USER_DB;

    }

    public static GameDatabase getGameDb() {
        return GAME_DB;
    }
}
