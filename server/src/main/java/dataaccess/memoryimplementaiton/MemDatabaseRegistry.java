package dataaccess.memoryimplementaiton;

public class MemDatabaseRegistry {
    private static final MemAuthDatabase AUTH_DB = new MemAuthDatabase();
    private static final MemUserDatabase USER_DB = new MemUserDatabase();
    private static final MemGameDatabase GAME_DB = new MemGameDatabase();

    public static MemAuthDatabase getAuthDb() {
        return AUTH_DB;
    }

    public static MemUserDatabase getUserDb() {
        return USER_DB;

    }

    public static MemGameDatabase getGameDb() {
        return GAME_DB;
    }
}
