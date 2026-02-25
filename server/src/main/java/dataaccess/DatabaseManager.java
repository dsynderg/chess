package dataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        logger.info("Creating database: {}", databaseName);
        logger.debug("SQL: {}", statement);
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
            logger.info("Database '{}' created successfully", databaseName);
        } catch (SQLException ex) {
            logger.error("Failed to create database '{}'", databaseName, ex);
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        logger.debug("Establishing database connection to: {}", databaseName);
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            logger.debug("Database connection established successfully");
            return conn;
        } catch (SQLException ex) {
            logger.error("Failed to establish database connection", ex);
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
    static boolean inDatabaseHelper(String gameName, String checkSql) throws DataAccessException {
        logger.debug("Checking database existence with SQL: {}", checkSql);
        logger.debug("Parameter: {}", gameName);
        try (Connection conn = DatabaseManager.getConnection();
        var statement = conn.prepareStatement(checkSql);) {
            statement.setString(1, gameName);
            var rs = statement.executeQuery();
            boolean exists = rs.next();
            logger.debug("Database check result: {}", exists);
            return exists;
        } catch (SQLException | DataAccessException e) {
            logger.error("Database check failed for: {}", gameName, e);
            throw new DataAccessException("There was a Database problem",e);
        }


    }
}
