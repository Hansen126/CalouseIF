package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Singleton class to manage database connections and execute queries.
 */
public class Singleton {

    private static Singleton instance;

    // Database connection configuration details
    private static final String HOST = "localhost:3306";
    private static final String DATABASE = "calouseif";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String CONNECTION_URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    // Instance variables for managing database connection and query results
    private Connection connection;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;

    /**
     * Private constructor to prevent instantiation.
     */
    private Singleton() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the database connection
            connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a query using the provided PreparedStatement.
     *
     * @param preparedStatement The PreparedStatement to execute.
     * @return                  The resulting ResultSet.
     */
    public ResultSet executeQuery(PreparedStatement preparedStatement) {
        try {
            // Execute the query and store the result
            resultSet = preparedStatement.executeQuery();
            // Retrieve and store metadata about the query results
            resultSetMetaData = resultSet.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Executes an update using the provided PreparedStatement.
     *
     * @param preparedStatement The PreparedStatement to execute.
     * @return                  The number of affected rows, or null if an error occurs.
     */
    public Integer executeUpdate(PreparedStatement preparedStatement) {
        try {
        	System.out.println("executeUpdate 1" + preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	System.out.println("executeUpdate 2");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Prepares a PreparedStatement for the given SQL query.
     *
     * @param query The SQL query to prepare.
     * @return      The prepared PreparedStatement, or null if an error occurs.
     */
    public PreparedStatement prepareStatement(String query) {
        try {
            // Create and return the PreparedStatement for the provided query
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the singleton instance of ConnectionSingleton.
     *
     * @return The singleton instance.
     */
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
