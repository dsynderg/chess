package dataaccess;

public class SQLGameDatabase {
    private final String[] createStatement = {
          """  
          CREATE TABLE IF NOT EXISTS GameData (
          gameID INT PRIMARY KEY,
          whiteUsername VARCHAR(255),
          blackUsername VARCHAR(255),
          gameName VARCHAR(255),
          game TEXT
          );
          """,
          """
          CREATE TABLE IF NOT EXISTS UserData (
          username VARCHAR(255),
          password VARCHAR(255),
          email VARCHAR(255)
           );
    
          """,
            """
            CREATE TABLE IF NOT EXISTS AuthData (
            authToken VARCHAR(255),
            username VARCHAR(255)
            );
            """
    };
    public enum databaseType{
        AUTH,
        GAME,
        USER

    }
}

