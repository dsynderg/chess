package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import modules.GameData;
import modules.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class SQLUserDatabase {
    public static boolean inDatabase(String username) throws SQLException, DataAccessException {
        String checkSql = "SELECT 1 FROM userdata WHERE username = ?";

        return DatabaseManager.inDatabaseHelper(username, checkSql);
        }



    public static boolean passwordUsernameMatch(String password, String username) throws DataAccessException, SQLException {
        String sql ="SELECT * FROM userdata WHERE username = ?";
        Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(sql);
        statement.setString(1,String.valueOf(username));
        var response = statement.executeQuery();
        while(response.next()){
            return BCrypt.checkpw(password, response.getString("password"));
        }
        return true;

    }

    public static boolean removeFromDatabase(User removeObject) throws DataAccessException, SQLException {
        String deleteStatement = "DELETE FROM userdata WHERE username = ?;";
        if(!inDatabase(removeObject.username())){
            return false;
        }
        Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(deleteStatement);
        statement.setString(1,String.valueOf(removeObject.username()));
        statement.executeUpdate();
        return true;

    }



    public static boolean addToDatabase(User addObject) throws SQLException, DataAccessException {
        String query = "INSERT INTO userdata " +
                "(username, password, email) " +
                "VALUES (?, ?, ?)";
        if(inDatabase(addObject.username())){
            return false;
        }
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, String.valueOf(addObject.username()));
        statement.setString(2, addObject.password());
        statement.setString(3, addObject.email());


        statement.executeUpdate();
        return true;

    }
    public static boolean deleteall() throws DataAccessException, SQLException {
        String query =  "DELETE FROM userdata;";
        Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();

        statement.executeUpdate(query);

        return true;

}

}



