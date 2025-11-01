package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDeleteDataBase {
    private static final String [] deleteStatements = {
            "DELETE FROM authdata;",
            "DELETE FROM gamedata;",
            "DELETE FROM userdata;"};
    public static boolean deleteAll() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();) {
            for (String query : deleteStatements) {
                statement.executeUpdate(query);
            }
            return true;
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("problem with database",e);
        }

    }

}
