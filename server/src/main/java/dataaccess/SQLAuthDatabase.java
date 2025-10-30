package dataaccess;

import modules.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLAuthDatabase  {
    public static String getUsername(String authToken){

        try(Connection conn = DatabaseManager.getConnection()){

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
        try(Connection conn = DatabaseManager.getConnection()){

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
        return false;
    }


    public static boolean deleteall() {
        try(Connection conn = DatabaseManager.getConnection()){

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
        return false;
    }

    public static void listDatabase(){
        try(Connection conn = DatabaseManager.getConnection()){

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
    }
    public static boolean inDatabase(String authToken){
        String checkSql = "SELECT 1 FROM AuthData WHERE authToken = ?";

        try(Connection conn = DatabaseManager.getConnection()){
            var statement = conn.prepareStatement(checkSql);
            statement.setString(1,authToken);
            var rs = statement.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }
}
