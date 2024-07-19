package com.thelabirinto.graphics;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;
import com.thelabirinto.connection.DbConnectionSingleton;
import com.thelabirinto.factory.ScreenFactory;
import com.thelabirinto.factory.ScreenType;

import javax.swing.*;
import java.awt.*;

/**
 * Frame sul quale sono mostrate tutte le schermate sotto forma di panel
 */
public class MainFrame extends JFrame {
    private final ScreenFactory screenFactory;
    private Maze maze;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private DbConnectionSingleton dbConnection;
    private Difficulty difficulty;
    int width;
    int height;


    public ScreenFactory getScreenFactory() {
        return screenFactory;
    }

    public Maze getMaze() {
        return maze;
    }

    /**
     * Costruttore
     * Si occupa anche di inizializzare la connessione col DB,
     * @param width
     * @param height
     */
    public MainFrame(int width, int height) {
        initializeDbConnection();
        this.width = width;
        this.height = height;
        screenFactory = new ScreenFactory(this);
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height); // Size settata manualmente prima della creazione del maze
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        //Aggiunge HomeScreen e NameInputScreen al Frame
        mainPanel.add(screenFactory.createScreen(ScreenType.HOME), "HomeScreen");
        mainPanel.add(screenFactory.createScreen(ScreenType.NAME_INPUT), "NameInputScreen");
        add(mainPanel);
        cardLayout.show(mainPanel, "HomeScreen");
    }

    /**
     * Inizializza tramite i metodi di dbConnection la connessione al DB
     * e associa il singleton al mainframe
     */
    private void initializeDbConnection() {
        try {
        dbConnection = DbConnectionSingleton.getInstance();
        dbConnection.buildConnection();
        dbConnection.createDatabase();
        }catch (Exception e) {
            e.printStackTrace();
        }
        }

    public DbConnectionSingleton getDbConnection() {
        return dbConnection;
    }

    /**
     * Permette di visualizzare una schermata sfruttando le proprietà di cardLayout
     * @param screenName
     */
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Utilizza la classe Builder "MazeBuilder" per costruire l'oggetto Maze
     * @param name nome del player
     * @param surname cognome del player
     * @param difficulty difficoltà selezionata dal player
     */
    public void createMaze(String name, String surname, Difficulty difficulty) {
        MazeBuilder builder;
        do {
            builder = new MazeBuilder(width, height, 64, difficulty);
            maze = builder.addEdges()
                    .addWalls()
                    .addExit()
                    .addRobot()
                    .setPlayer(name, surname)
                    .build();
        } while (maze.isPlayable()); //esegue la creazione del maze finchè la mappa non è giocabile
        mainPanel.add(screenFactory.createScreen(ScreenType.MAZE), "MazeScreen");
        setSize(maze.getWindowWidth(), maze.getWindowHeight());
        setLocationRelativeTo(null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Rende il componente focusable, e quindi pronto a ricevere input dall'utente
     */
    public void makeFocus() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof MazeScreen) {
                comp.requestFocusInWindow();
            }
        }
    }
}
