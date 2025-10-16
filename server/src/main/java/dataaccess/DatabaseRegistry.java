package dataaccess;

public class DatabaseRegistry {
    private static final AuthDatabase authDB = new AuthDatabase();
    private static final UserDatabase userDB = new UserDatabase();
    private static final GameDatabase gameDB = new GameDatabase();

    public static AuthDatabase getAuthDB() {return authDB;}
    public static UserDatabase getUserDB() {return userDB;}
    public static GameDatabase getGameDB() {return gameDB;}
}
