package com.thelabirinto.graphics;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;

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
    private double difficulty;

    public Maze getMaze() {
        return maze;
    }

    public MainFrame() {
        homeScreen = new HomeScreen(this);
        highScoreScreen = new HighScoreScreen(this);
        nameInputScreen = new NameInputScreen(this);
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // Default size before maze creation
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Add screens to the main panel
        mainPanel.add(homeScreen, "HomeScreen");
        mainPanel.add(highScoreScreen, "HighScoreScreen");
        mainPanel.add(nameInputScreen, "NameInputScreen");
        add(mainPanel);

        // Show home screen initially
        cardLayout.show(mainPanel, "HomeScreen");
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

    public void createMaze() {
        MazeBuilder builder;
        do {
            builder = new MazeBuilder(600, 600, 64,this.getDifficulty());
            maze = builder.addEdges()
                    .addWalls()
                    .addExit()
                    .addRobot()
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
}
