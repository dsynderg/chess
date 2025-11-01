package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLTableControler {
    public static void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();

        for(String statement: createStatementList){
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(statement);){
             prepStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("database issue",e);
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



}

