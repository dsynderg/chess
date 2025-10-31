package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDatabase  {
    public static String getUsername(String authToken){
        String checkSql = "SELECT username FROM AuthData WHERE authToken = ?";
        try(Connection conn = DatabaseManager.getConnection()){
        var statement = conn.prepareStatement(checkSql);
        statement.setString(1,authToken);
        var response = statement.executeQuery();
        if(response.next()){
            return response.getString("username");
        }
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

        return null;
    }




    public static boolean addToDatabase(AuthData addObject) {
        String query = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        if(inDatabase(addObject.authToken())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,addObject.authToken());
            statement.setString(2,addObject.username());
            statement.executeUpdate();
            return true;
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }


    public static boolean removeFromDatabase(AuthData removeObject) {
        String deleteStatement = "DELETE FROM AuthData WHERE authToken = ?;";
        if(!inDatabase(removeObject.authToken())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = conn.prepareStatement(deleteStatement);
            statement.setString(1,removeObject.authToken());
            statement.executeUpdate();
            return true;

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }


    public static boolean deleteall() {
        String query =  "DELETE FROM authdata;";
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement statement = conn.createStatement();

            statement.executeUpdate(query);

            return true;
        } catch (SQLException| DataAccessException e) {
            throw new RuntimeException("Failed to get a connection", e);
        }
    }

    public static void listDatabase(){
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = conn.prepareStatement("select * from authdata;");
            var response = statement.executeQuery();
            while(response.next()){
                String auth = response.getString("authToken");
                String username = response.getString("username");
                System.out.println("authToken: "+auth+", username: "+username);
            }
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
    }
    public static boolean inDatabase(String authToken){
        String checkSql = "SELECT 1 FROM AuthData WHERE authToken = ?";

        return DatabaseManager.inDatabaseHelper(authToken, checkSql);

    }
}
