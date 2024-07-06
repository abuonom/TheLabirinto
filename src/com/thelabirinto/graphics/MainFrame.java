package com.thelabirinto.graphics;

import com.thelabirinto.builder.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    private Maze maze;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    HomeScreen homeScreen;
    HighScoreScreen highScoreScreen;
    NameInputScreen nameInputScreen;
    MazeScreen mazeScreen;

    public Maze getMaze() {
        return maze;
    }

    public MainFrame(Maze maze) {
        this.maze = maze;
        homeScreen = new HomeScreen(this);
        highScoreScreen = new HighScoreScreen(this);
        nameInputScreen = new NameInputScreen(this);
        mazeScreen = new MazeScreen(this);
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(maze.getWindowWidth(), maze.getWindowHeight());
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Add screens to the main panel
        mainPanel.add(homeScreen, "HomeScreen");
        mainPanel.add(highScoreScreen, "HighScoreScreen");
        mainPanel.add(nameInputScreen, "NameInputScreen");
        mainPanel.add(mazeScreen, "MazeScreen");
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

    public MazeScreen getMazeScreen() {
        return mazeScreen;
    }
}

