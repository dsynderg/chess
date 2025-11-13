package dataaccess;

import modules.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDatabase {
    public static boolean inDatabase(String username) throws DataAccessException {
        String checkSql = "SELECT 1 FROM userdata WHERE username = ?";

        return DatabaseManager.inDatabaseHelper(username, checkSql);
        }



    public static boolean passwordUsernameMatch(String password, String username) throws DataAccessException {
        String sql ="SELECT * FROM userdata WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(sql);) {
            statement.setString(1, username);
            var response = statement.executeQuery();
            while (response.next()) {
                return BCrypt.checkpw(password, response.getString("password"));
            }
            return false;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Database problems",e);
        }

    }

    public static boolean removeFromDatabase(User removeObject) throws DataAccessException {
        String deleteStatement = "DELETE FROM userdata WHERE username = ?;";
        if(!inDatabase(removeObject.username())){
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);) {
            statement.setString(1, String.valueOf(removeObject.username()));
            statement.executeUpdate();
            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("",e);
        }

    }



    public static boolean addToDatabase(User addObject) throws DataAccessException {
        String query = "INSERT INTO userdata " +
                "(username, password, email) " +
                "VALUES (?, ?, ?)";
        if(inDatabase(addObject.username())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
            statement.setString(1, String.valueOf(addObject.username()));
            statement.setString(2, addObject.password());
            statement.setString(3, addObject.email());


            statement.executeUpdate();
            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Database problem",e);
        }

    }
    public static boolean deleteall() throws DataAccessException {
        String query =  "DELETE FROM userdata;";

        try(Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);

            return true;

        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("there was a data access issue",e);
        }



}

}



