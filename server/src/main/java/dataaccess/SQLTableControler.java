package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLTableControler {
    public static void initialize() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        for(String statement: createStatementList){


            PreparedStatement prepStatement = null;
            try {
                prepStatement = conn.prepareStatement(statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                prepStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }
    private static final String[] createStatementList = {
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


    public static enum tableType{
        AUTH,
        GAME,
        USER

    }
}

