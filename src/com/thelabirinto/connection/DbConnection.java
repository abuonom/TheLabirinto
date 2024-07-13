package com.thelabirinto.connection;

import com.thelabirinto.builder.Player;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
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
                switchToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL connection error
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                buildConnection();
            } else {
                switchToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void switchToDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("USE thelabirinto");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS thelabirinto";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "moves INT, " +
                "difficulty DOUBLE)";

        try (Connection conn = getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDatabaseSQL);
            switchToDatabase();  // Ensure the connection is using the correct database
            stmt.executeUpdate(createTableSQL);
            System.out.println("Database and table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
    }

    public void insertPlayer(Player player) {
        String sqlPlayerInsert = "INSERT INTO player(first_name, last_name, moves, difficulty) VALUES (?, ?, ?, ?)";
        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlPlayerInsert)) {

            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getSurname());
            preparedStatement.setInt(3, player.getMoves());
            preparedStatement.setDouble(4, player.getDifficulty());
            preparedStatement.executeUpdate();
            System.out.println("Player inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
    }

    public ArrayList<Player> getTopPlayersByDifficulty(double difficulty) {
        System.out.println(difficulty);
        String sqlSelectTop5 = "SELECT * FROM player WHERE difficulty = ? ORDER BY moves ASC LIMIT 50";
        ArrayList<Player> topPlayers = new ArrayList<>();
        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectTop5)) {

            preparedStatement.setDouble(1, difficulty);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Player player = new Player(rs.getString("first_name"),
                        rs.getString("last_name"), rs.getInt("moves"));
                topPlayers.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception: SQL execution error
        }
        return topPlayers;
    }
}
