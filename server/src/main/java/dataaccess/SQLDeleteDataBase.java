package dataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLDeleteDataBase {
    private static final Logger logger = LoggerFactory.getLogger(SQLDeleteDataBase.class);
    private static final String [] DELETE_STATEMENTS = {
            "DELETE FROM authdata;",
            "DELETE FROM gamedata;",
            "DELETE FROM userdata;"};
    public static boolean deleteAll() throws DataAccessException {
        logger.info("Deleting all data from all tables");
        try (Connection conn = DatabaseManager.getConnection();
        Statement statement = conn.createStatement();) {
            for (String query : DELETE_STATEMENTS) {
                logger.debug("Executing delete SQL: {}", query);
                statement.executeUpdate(query);
            }
            logger.info("Successfully deleted all data from all tables");
            return true;
        } catch (SQLException | DataAccessException e) {
            logger.error("Failed to delete all data", e);
            throw new DataAccessException("problem with database",e);
        }

    }

}
