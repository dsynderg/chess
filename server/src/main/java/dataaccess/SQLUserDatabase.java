package dataaccess;

import modules.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(SQLUserDatabase.class);
    public static boolean inDatabase(String username) throws DataAccessException {
        String checkSql = "SELECT 1 FROM userdata WHERE username = ?";
        logger.debug("Checking if user exists: {}", username);

        return DatabaseManager.inDatabaseHelper(username, checkSql);
        }



    public static boolean passwordUsernameMatch(String password, String username) throws DataAccessException {
        String sql ="SELECT * FROM userdata WHERE username = ?";
        logger.debug("Verifying password for user: {}", username);
        logger.debug("SQL: {}", sql);
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(sql);) {
            statement.setString(1, username);
            var response = statement.executeQuery();
            while (response.next()) {
                boolean match = BCrypt.checkpw(password, response.getString("password"));
                logger.info("Password verification for user '{}': {}", username, match ? "success" : "failed");
                return match;
            }
            logger.info("User '{}' not found during password verification", username);
            return false;
        } catch (SQLException | DataAccessException e) {
            logger.error("Password verification failed for user: {}", username, e);
            throw new DataAccessException("Database problems",e);
        }

    }

    public static boolean removeFromDatabase(User removeObject) throws DataAccessException {
        String deleteStatement = "DELETE FROM userdata WHERE username = ?;";
        logger.debug("Removing user from database: {}", removeObject.username());
        logger.debug("SQL: {}", deleteStatement);
        if(!inDatabase(removeObject.username())){
            logger.info("User '{}' not found in database", removeObject.username());
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);) {
            statement.setString(1, String.valueOf(removeObject.username()));
            statement.executeUpdate();
            logger.info("Successfully removed user: {}", removeObject.username());
            return true;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to remove user: {}", removeObject.username(), e);
            throw new DataAccessException("",e);
        }

    }



    public static boolean addToDatabase(User addObject) throws DataAccessException {
        String query = "INSERT INTO userdata " +
                "(username, password, email) " +
                "VALUES (?, ?, ?)";
        logger.debug("Adding user to database: {}", addObject.username());
        logger.debug("SQL: {}", query);
        if(inDatabase(addObject.username())){
            logger.info("User '{}' already exists in database", addObject.username());
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
            statement.setString(1, String.valueOf(addObject.username()));
            statement.setString(2, addObject.password());
            statement.setString(3, addObject.email());


            statement.executeUpdate();
            logger.info("Successfully added user: {}", addObject.username());
            return true;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to add user: {}", addObject.username(), e);
            throw new DataAccessException("Database problem",e);
        }

    }
    public static boolean deleteall() throws DataAccessException {
        String query =  "DELETE FROM userdata;";
        logger.info("Deleting all users from database");
        logger.debug("SQL: {}", query);

        try(Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);
            logger.info("Successfully deleted all users");

            return true;

        } catch (DataAccessException | SQLException e) {
            logger.error("Failed to delete all users", e);
            throw new DataAccessException("there was a data access issue",e);
        }



}

}



