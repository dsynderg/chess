package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDatabase  {
    public static String getUsername(String authToken) throws DataAccessException, SQLException {
        String checkSql = "SELECT username FROM AuthData WHERE authToken = ?";
        Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(checkSql);
        statement.setString(1,authToken);
        var response = statement.executeQuery();
        if(response.next()){
            return response.getString("username");
        }
        return null;

    }




    public static boolean addToDatabase(AuthData addObject) throws DataAccessException, SQLException {
        String query = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        if(inDatabase(addObject.authToken())){
            return false;
        }
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1,addObject.authToken());
        statement.setString(2,addObject.username());
        statement.executeUpdate();
        return true;


    }


    public static boolean removeFromDatabase(AuthData removeObject) throws DataAccessException, SQLException {
        String deleteStatement = "DELETE FROM AuthData WHERE authToken = ?;";
        if(!inDatabase(removeObject.authToken())){
            return false;
        }
        Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);
        statement.setString(1,removeObject.authToken());
        statement.executeUpdate();
        return true;


    }


    public static boolean deleteall() throws SQLException, DataAccessException {
        String query =  "DELETE FROM authdata;";
        Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();

        statement.executeUpdate(query);

        return true;

    }

    public static void listDatabase() throws DataAccessException, SQLException {
        Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement("select * from authdata;");
        var response = statement.executeQuery();
        while(response.next()){
            String auth = response.getString("authToken");
            String username = response.getString("username");
            System.out.println("authToken: "+auth+", username: "+username);
        }

    }
    public static boolean inDatabase(String authToken) throws SQLException, DataAccessException {
        String checkSql = "SELECT 1 FROM AuthData WHERE authToken = ?";

        return DatabaseManager.inDatabaseHelper(authToken, checkSql);

    }
}
