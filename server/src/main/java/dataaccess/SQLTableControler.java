package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLTableControler {
    public static void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();

        for(String statement: CREATE_STATEMENT_LIST){
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(statement);){
             prepStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("database issue",e);
        }
        }


    }
    //gameID INT,
    private static final String[] CREATE_STATEMENT_LIST = {
          """  
          CREATE TABLE IF NOT EXISTS gamedata (
          gamenumber INT AUTO_INCREMENT PRIMARY KEY,
          gameID int,
          whiteUsername VARCHAR(255),
          blackUsername VARCHAR(255),
          gameName VARCHAR(255),
          game TEXT
          );
          """,
          """
          CREATE TABLE IF NOT EXISTS userdata (
          username VARCHAR(255),
          password VARCHAR(255),
          email VARCHAR(255)
           );
    
          """,
            """
            CREATE TABLE IF NOT EXISTS authdata (
            authToken VARCHAR(255),
            username VARCHAR(255)
            );
            """
    };



}

