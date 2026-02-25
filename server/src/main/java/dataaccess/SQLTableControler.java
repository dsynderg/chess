package dataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLTableControler {
    private static final Logger logger = LoggerFactory.getLogger(SQLTableControler.class);
    public static void initialize() throws DataAccessException {
        logger.info("Initializing database tables");
        DatabaseManager.createDatabase();

        for(String statement: CREATE_STATEMENT_LIST){
        logger.debug("Executing table creation SQL: {}", statement.trim());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStatement = conn.prepareStatement(statement);){
             prepStatement.executeUpdate();
             logger.info("Table creation SQL executed successfully");
        } catch (DataAccessException | SQLException e) {
            logger.error("Failed to execute table creation SQL", e);
            throw new DataAccessException("database issue",e);
        }
        }
        logger.info("Database tables initialized successfully");


    }
    //gameID INT,
    private static final String[] CREATE_STATEMENT_LIST = {
          """  
          CREATE TABLE IF NOT EXISTS gamedata (
          gameID INT AUTO_INCREMENT PRIMARY KEY,
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

