package com.thelabirinto.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbConnection {

    private static final String PROPERTIES_FILE = "db.properties";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private Connection connection = null;
    private static DbConnection dbConnectionInstance;

    private DbConnection() {
        loadProperties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception: Driver class not found
        }
    }

    private void loadProperties() {
        InputStream input = null;
        try {
            // Use the ClassLoader to load the properties file from the classpath
            input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }

            // Load the properties from the input stream
            Properties prop = new Properties();
            prop.load(input);

            // Retrieve the property values
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPassword = prop.getProperty("db.password");

            // Validate the properties
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new IOException("Missing database configuration in " + PROPERTIES_FILE);
            }
        } catch (IOException ex) {
            // Print the stack trace for debugging purposes
            ex.printStackTrace();

            // Handle the exception: print a user-friendly error message
            System.err.println("Failed to load database properties file: " + ex.getMessage());
        } finally {
            // Ensure the input stream is closed to prevent resource leaks
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static DbConnection getInstance() {
        if (dbConnectionInstance == null) {
            dbConnectionInstance = new DbConnection();
        }
        return dbConnectionInstance;
    }

    public void buildConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL connection error
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createDatabase() {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS thelabirinto";
        String useDatabaseSQL = "USE thelabirinto";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "moves INT, " +
                "difficulty VARCHAR(50))";

        try (Connection conn = getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDatabaseSQL);
            stmt.executeUpdate(useDatabaseSQL);
            stmt.executeUpdate(createTableSQL);
            System.out.println("Database and table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
    }

    public void insertPlayer(String firstName, String lastName, int moves, String difficulty) {
        String sqlPlayerInsert = "INSERT INTO player(first_name, last_name, moves, difficulty) VALUES (?, ?, ?, ?)";

        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlPlayerInsert)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, moves);
            preparedStatement.setString(4, difficulty);
            preparedStatement.executeUpdate();
            System.out.println("Player inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
    }

    public void getTop5PlayersByDifficulty(String difficulty) {
        String sqlSelectTop5 = "SELECT * FROM player WHERE difficulty = ? ORDER BY moves ASC LIMIT 5";

        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectTop5)) {

            preparedStatement.setString(1, difficulty);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", First Name: " + rs.getString("first_name") +
                        ", Last Name: " + rs.getString("last_name") +
                        ", Moves: " + rs.getInt("moves") +
                        ", Difficulty: " + rs.getString("difficulty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
    }
}
