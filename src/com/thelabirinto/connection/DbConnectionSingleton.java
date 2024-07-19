package com.thelabirinto.connection;

import com.thelabirinto.builder.Player;
import com.thelabirinto.graphics.Difficulty;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Classe creata seguendo pattern Singleton per gestire la connessione al database.
 * Fornisce metodi per connettersi al database, creare tabelle ed eseguire operazioni CRUD.
 */
public class DbConnectionSingleton {

    private static final String PROPERTIES_FILE = "db.properties";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private Connection connection = null;
    private static DbConnectionSingleton dbConnectionInstance;

    /**
     * Costruttore privato per prevenire l'istanza.
     * Carica le proprietà del database e inizializza il driver JDBC.
     */
    private DbConnectionSingleton() {
        loadProperties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica le proprietà del database da un file di proprietà.
     */
    private void loadProperties() {
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (input == null) {
                throw new IOException("Impossibile trovare " + PROPERTIES_FILE);
            }

            // Carica le proprietà dall'input stream
            Properties prop = new Properties();
            prop.load(input);

            // Recupera i valori delle proprietà
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPassword = prop.getProperty("db.password");

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new IOException("Configurazione del database mancante in " + PROPERTIES_FILE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Caricamento del file di proprietà del database fallito: " + ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Restituisce l'istanza singleton della connessione al database.
     *
     * @return l'istanza singleton di DbConnectionSingleton
     */
    public static DbConnectionSingleton getInstance() {
        if (dbConnectionInstance == null) {
            dbConnectionInstance = new DbConnectionSingleton();
        }
        return dbConnectionInstance;
    }

    /**
     * Stabilisce una connessione al database.
     */
    public void buildConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                switchToDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce la connessione corrente al database. Se la connessione è chiusa o nulla, tenterà di riconnettersi.
     *
     * @return la connessione corrente al database
     */
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

    /**
     * Cambia la connessione al database specificato.
     *
     * @throws SQLException se si verifica un errore di accesso al database
     */
    private void switchToDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("USE thelabirinto");
        }
    }

    /**
     * Chiude la connessione corrente al database.
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

    /**
     * Crea il database e le tabelle se non esistono già.
     */
    public void createDatabase() {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS thelabirinto";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "last_name VARCHAR(50), " +
                "moves INT, " +
                "difficulty VARCHAR(50))";

        try (Connection conn = getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDatabaseSQL);
            switchToDatabase();  // Si assicura che la connessione utilizzi il database corretto
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserisce un nuovo giocatore nel database.
     *
     * @param player il giocatore da inserire
     * @return true se l'inserimento è riuscito, false altrimenti
     */
    public boolean insertPlayer(Player player) {
        String sqlPlayerInsert = "INSERT INTO player(first_name, last_name, moves, difficulty) VALUES (?, ?, ?, ?)";
        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlPlayerInsert)) {

            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getSurname());
            preparedStatement.setInt(3, player.getMoves());
            preparedStatement.setString(4, player.getDifficulty());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera i primi 50 giocatori per numero di mosse per un dato livello di difficoltà.
     *
     * @param difficulty il livello di difficoltà da filtrare
     * @return una lista dei migliori giocatori per la difficoltà specificata
     */
    public ArrayList<Player> getTopPlayersByDifficulty(Difficulty difficulty) {
        String sqlSelectTop5 = "SELECT * FROM player WHERE difficulty = ? ORDER BY moves ASC LIMIT 50";
        ArrayList<Player> topPlayers = new ArrayList<>();
        try (Connection conn = getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlSelectTop5)) {

            preparedStatement.setString(1, difficulty.getName());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Player player = new Player(rs.getString("first_name"),
                        rs.getString("last_name"), rs.getInt("moves"), rs.getString("difficulty"));
                topPlayers.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topPlayers;
    }
}
