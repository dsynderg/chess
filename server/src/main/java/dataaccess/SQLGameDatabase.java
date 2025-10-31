package dataaccess;

import modules.AuthData;
import modules.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLGameDatabase {


    public static boolean addToDatabase(GameData addObject) {
        String query = "INSERT INTO gamedata " +
                "(gameID, whiteUsername, blackUsername, gameName, game) " +
                "VALUES (?, ?, ?, ?, ?)";
        if(inDatabase(addObject.gameName())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, String.valueOf(addObject.gameID()));
            statement.setString(2, addObject.whiteUsername());
            statement.setString(3, addObject.blackUsername());
            statement.setString(4, addObject.gameName());
            statement.setString(5, addObject.game().toString());


            statement.executeUpdate();
            return true;
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }


    public static boolean removeFromDatabase(GameData removeObject) {
        String deleteStatement = "DELETE FROM gamedata WHERE gameID = ?;";
        if(!inDatabase(removeObject.gameName())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = conn.prepareStatement(deleteStatement);
            statement.setString(1,String.valueOf(removeObject.gameID()));
            statement.executeUpdate();
            return true;

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }


    public static boolean deleteAll() {
        String query =  "DELETE FROM gamedata;";
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
            var statement = conn.prepareStatement("select * from gamedata;");
            var response = statement.executeQuery();
            while(response.next()){
                int gameID= response.getInt("gameID");
                String whiteUsername = response.getString("whiteUsername");
                String blackUsername = response.getString("blackUsername");
                String gameName = response.getString("gameName");
                String game = response.getString("game");
                System.out.println("gameID: " + String.valueOf(gameID)+
                                    ", White: "+ whiteUsername+
                                    ", Black: "+ blackUsername+
                                    ", Name: "+ gameName +
                                    ", Game: "+game);
            }
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
    }
    public static boolean inDatabase(String gameName){
        String checkSql = "SELECT 1 FROM gamedata WHERE gameName = ?";

        return DatabaseManager.inDatabaseHelper(gameName, checkSql);

    }


}
