package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import modules.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLGameDatabase {

    public static Boolean updateDatabase(GameData addObject) throws DataAccessException {
        String query = "INSERT INTO gamedata " +
                "(gameID, whiteUsername, blackUsername, gameName, game) " +
                "VALUES (?, ?, ?, ?, ?)";
        if (inDatabase(addObject.gameName())) {
            return false;
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(query);
            Gson gson = new Gson();
            String gamejson = gson.toJson(addObject.game());
            statement.setString(1, String.valueOf(addObject.gameID()));
            statement.setString(2, addObject.whiteUsername());
            statement.setString(3, addObject.blackUsername());
            statement.setString(4, addObject.gameName());
            statement.setString(5, gamejson);

            int sqlRow = statement.executeUpdate();
            return true;

        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }
    }

    public static GameData addToDatabase(GameData addObject) throws DataAccessException {
        String query = "INSERT INTO gamedata " +
                "(whiteUsername, blackUsername, gameName, game) " +
                "VALUES ( ?, ?, ?, ?)";
        String getsname = "SELECT * FROM gamedata WHERE gameName = ?";
        if(inDatabase(addObject.gameName())){
            return null;
        }
        try(Connection conn = DatabaseManager.getConnection()){
            PreparedStatement statement = conn.prepareStatement(query);
            PreparedStatement getgameobject = conn.prepareStatement(getsname);
            Gson gson = new Gson();
            String json = gson.toJson(addObject.game());
//            statement.setString(1, String.valueOf(addObject.gameID()));
            statement.setString(1, addObject.whiteUsername());
            statement.setString(2, addObject.blackUsername());
            statement.setString(3, addObject.gameName());
            statement.setString(4, json);

            int sqlRow = statement.executeUpdate();
            getgameobject.setString(1,addObject.gameName());
            var rs = getgameobject.executeQuery();
            if(rs.next()){
                int id = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                addObject = new GameData(id, whiteUsername,blackUsername,gameName,game);

            }
            return addObject;
        }
        catch(SQLException|DataAccessException e){
            throw new RuntimeException("There was a database connection issue",e);
        }

    }


    public static boolean removeFromDatabase(GameData removeObject) throws DataAccessException {
        String deleteStatement = "DELETE FROM gamedata WHERE gameID = ?;";
        if(!inDatabase(removeObject.gameName())){
            return false;
        }
        try(Connection conn = DatabaseManager.getConnection();
            var statement = conn.prepareStatement(deleteStatement);){

            statement.setString(1,String.valueOf(removeObject.gameID()));
            statement.executeUpdate();
            return true;

        }
        catch(SQLException|DataAccessException e){
            throw new DataAccessException("There was a database connection issue",e);
        }

    }


    public static boolean deleteAll() throws DataAccessException {
        String query =  "DELETE FROM gamedata;";
        try (Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement()) {
            statement.executeUpdate(query);

            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Database problem",e);
        }
        }


    public static ArrayList<GameData> listDatabase() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement("select * from gamedata;");
        var response = statement.executeQuery();) {
            Gson gson = new Gson();
            ArrayList<GameData> returnList = new ArrayList<>();
            while (response.next()) {
                int gameID = response.getInt("gameID");
                String whiteUsername = response.getString("whiteUsername");
                String blackUsername = response.getString("blackUsername");
                String gameName = response.getString("gameName");
                ChessGame game = gson.fromJson(response.getString("game"), ChessGame.class);
                returnList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
//                System.out.println("gameID: " + String.valueOf(gameID)+
//                                    ", White: "+ whiteUsername+
//                                    ", Black: "+ blackUsername+
//                                    ", Name: "+ gameName +
//                                    ", Game: "+game);
            }
            return returnList;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Database problem",e);
        }
    }
    public static boolean inDatabase(String gameName) throws DataAccessException {
        String checkSql = "SELECT 1 FROM gamedata WHERE gameName = ?";

        return DatabaseManager.inDatabaseHelper(gameName, checkSql);

    }
    public static GameData inDatabaseID(int gameID) throws DataAccessException {
        String checkSql = "SELECT * FROM gamedata WHERE gameID = ?";
        Gson gson = new Gson();
        try(Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(checkSql);) {
            statement.setInt(1, gameID);
            var rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(id, whiteUsername, blackUsername, gameName, game);
            } else {
                return null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Database problem",e);
        }

    }


}
