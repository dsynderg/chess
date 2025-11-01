package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDatabase  {
    public static String getUsername(String authToken) throws DataAccessException {
        String checkSql = "SELECT username FROM AuthData WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(checkSql);) {
            statement.setString(1, authToken);
            var response = statement.executeQuery();
            if (response.next()) {
                return response.getString("username");
            }
            return null;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("data problem",e);
        }

    }




    public static boolean addToDatabase(AuthData addObject) throws DataAccessException {
        String query = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        if(inDatabase(addObject.authToken())){
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);){
            statement.setString(1, addObject.authToken());
            statement.setString(2, addObject.username());
            statement.executeUpdate();
            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("database problem",e);
        }


    }


    public static boolean removeFromDatabase(AuthData removeObject) throws DataAccessException {
        String deleteStatement = "DELETE FROM AuthData WHERE authToken = ?;";
        if(!inDatabase(removeObject.authToken())){
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);) {
            statement.setString(1, removeObject.authToken());
            statement.executeUpdate();
            return true;
        } catch (DataAccessException| SQLException e) {
            throw new DataAccessException("database problem",e);
        }


    }


    public static boolean deleteall() throws DataAccessException {
        String query =  "DELETE FROM AuthData;";
        try(Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();) {

            statement.executeUpdate(query);

            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("DataAccess issue",e);
        }


    }

    public static void listDatabase() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement("select * from AuthData;");) {
            var response = statement.executeQuery();
            while (response.next()) {
                String auth = response.getString("authToken");
                String username = response.getString("username");
                System.out.println("authToken: " + auth + ", username: " + username);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("there was a database error",e);
        }

    }
    public static boolean inDatabase(String authToken) throws DataAccessException {
        String checkSql = "SELECT 1 FROM AuthData WHERE authToken = ?";

        return DatabaseManager.inDatabaseHelper(authToken, checkSql);

    }
}
