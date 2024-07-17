package com.thelabirinto.graphics;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;
import com.thelabirinto.connection.DbConnectionSingleton;
import com.thelabirinto.factory.ScreenFactory;
import com.thelabirinto.factory.ScreenType;

import javax.swing.*;
import java.awt.*;

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

    public MainFrame(int width, int height) {
        initializeDbConnection();
        this.width = width;
        this.height = height;
        screenFactory = new ScreenFactory(this);
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height); // Default size before maze creation
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Add screens to the main panel
        mainPanel.add(screenFactory.createScreen(ScreenType.HOME), "HomeScreen");
        mainPanel.add(screenFactory.createScreen(ScreenType.NAME_INPUT), "NameInputScreen");
        add(mainPanel);
        // Show home screen initially
        cardLayout.show(mainPanel, "HomeScreen");
    }

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

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

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
        } while (maze.isPlayable());
        mainPanel.add(screenFactory.createScreen(ScreenType.MAZE), "MazeScreen");
        setSize(maze.getWindowWidth(), maze.getWindowHeight());
        setLocationRelativeTo(null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void makeFocus() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof MazeScreen) {
                comp.requestFocusInWindow();
            }
        }
    }
}
