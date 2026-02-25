package dataaccess;

import modules.AuthData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDatabase  {
    private static final Logger logger = LoggerFactory.getLogger(SQLAuthDatabase.class);
    public static String getUsername(String authToken) throws DataAccessException {
        String checkSql = "SELECT username FROM authdata WHERE authToken = ?";
        logger.debug("Getting username for authToken");
        logger.debug("SQL: {}", checkSql);
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(checkSql);) {
            statement.setString(1, authToken);
            var response = statement.executeQuery();
            if (response.next()) {
                String username = response.getString("username");
                logger.info("Found username: {}", username);
                return username;
            }
            logger.info("No username found for provided authToken");
            return null;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to get username", e);
            throw new DataAccessException("data problem",e);
        }

    }




    public static boolean addToDatabase(AuthData addObject) throws DataAccessException {
        String query = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";
        logger.debug("Adding auth data to database");
        logger.debug("SQL: {}", query);
        logger.debug("Username: {}", addObject.username());
        if(inDatabase(addObject.authToken())){
            logger.info("AuthToken already exists in database");
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);){
            statement.setString(1, addObject.authToken());
            statement.setString(2, addObject.username());
            statement.executeUpdate();
            logger.info("Successfully added auth data for user: {}", addObject.username());
            return true;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to add auth data for user: {}", addObject.username(), e);
            throw new DataAccessException("database problem",e);
        }


    }


    public static boolean removeFromDatabase(AuthData removeObject) throws DataAccessException {
        String deleteStatement = "DELETE FROM authdata WHERE authToken = ?;";
        logger.debug("Removing auth data from database");
        logger.debug("SQL: {}", deleteStatement);
        if(!inDatabase(removeObject.authToken())){
            logger.info("AuthToken not found in database");
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);) {
            statement.setString(1, removeObject.authToken());
            statement.executeUpdate();
            logger.info("Successfully removed auth data");
            return true;
        } catch (DataAccessException| SQLException e) {
            logger.error("Failed to remove auth data", e);
            throw new DataAccessException("database problem",e);
        }


    }


    public static boolean deleteall() throws DataAccessException {
        String query =  "DELETE FROM authdata;";
        logger.info("Deleting all auth data from database");
        logger.debug("SQL: {}", query);
        try(Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();) {

            statement.executeUpdate(query);
            logger.info("Successfully deleted all auth data");

            return true;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to delete all auth data", e);
            throw new DataAccessException("DataAccess issue",e);
        }


    }

    public static void listDatabase() throws DataAccessException {
        logger.debug("Listing all auth data");
        try(Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement("select * from authdata;");) {
            var response = statement.executeQuery();
            int count = 0;
            while (response.next()) {
                String auth = response.getString("authToken");
                String username = response.getString("username");
                System.out.println("authToken: " + auth + ", username: " + username);
                count++;
            }
            logger.info("Listed {} auth data entries", count);
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to list auth data", e);
            throw new DataAccessException("there was a database error",e);
        }

    }
    public static boolean inDatabase(String authToken) throws DataAccessException {
        String checkSql = "SELECT 1 FROM authdata WHERE authToken = ?";
        logger.debug("Checking if authToken exists in database");

        return DatabaseManager.inDatabaseHelper(authToken, checkSql);

    }
}
