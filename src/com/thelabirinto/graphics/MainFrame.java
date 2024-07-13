package com.thelabirinto.graphics;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;
import com.thelabirinto.connection.DbConnection;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Maze maze;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    HomeScreen homeScreen;
    HighScoreScreen highScoreScreen;
    NameInputScreen nameInputScreen;
    MazeScreen mazeScreen;
    private DbConnection dbConnection;
    private double difficulty;
    int width;
    int height;

    public Maze getMaze() {
        return maze;
    }

    public MainFrame(int width, int height) {
        initializeDbConnection();
        homeScreen = new HomeScreen(this);
        nameInputScreen = new NameInputScreen(this);
        this.width = width;
        this.height = height;
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height); // Default size before maze creation
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Add screens to the main panel
        mainPanel.add(homeScreen, "HomeScreen");
        mainPanel.add(nameInputScreen, "NameInputScreen");
        add(mainPanel);

        // Initialize DbConnection

        // Show home screen initially
        cardLayout.show(mainPanel, "HomeScreen");
    }

    private void initializeDbConnection() {
        dbConnection = DbConnection.getInstance();
        dbConnection.buildConnection();
        dbConnection.createDatabase();
        System.out.println(dbConnection);
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public HomeScreen getHomeScreen() {
        return homeScreen;
    }

    public HighScoreScreen getHighScoreScreen() {
        return highScoreScreen;
    }

    public NameInputScreen getNameInputScreen() {
        return nameInputScreen;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void createMaze(String name, String surname, double difficulty) {
        MazeBuilder builder;
        do {
            builder = new MazeBuilder(width, height, 64, this.getDifficulty());
            maze = builder.addEdges()
                    .addWalls()
                    .addExit()
                    .addRobot()
                    .setPlayer(name, surname)
                    .build();
        } while (!maze.isPlayable());
        mazeScreen = new MazeScreen(this);
        mainPanel.add(mazeScreen, "MazeScreen");
        setSize(maze.getWindowWidth(), maze.getWindowHeight());
        setLocationRelativeTo(null);
    }

    public MazeScreen getMazeScreen() {
        return mazeScreen;
    }
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setHighScoreScreen(HighScoreScreen highScoreScreen) {
        this.highScoreScreen = highScoreScreen;
        mainPanel.add(highScoreScreen, "HighScoreScreen");
    }
}
