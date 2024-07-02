package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    HomeScreen homeScreen = new HomeScreen(this);
    HighScoreScreen highScoreScreen = new HighScoreScreen(this);
    NameInputScreen nameInputScreen = new NameInputScreen(this);
    MazeScreen mazeScreen = new MazeScreen(this);

    public MainFrame() {
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
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

