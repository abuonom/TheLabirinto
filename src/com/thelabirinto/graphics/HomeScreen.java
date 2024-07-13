package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {
    private final MainFrame mainFrame;

    public HomeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeLayout();
    }

    private void initializeLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("TheLabirinto");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(titleLabel, gbc);

        // Add the image below the title
        ImageIcon startIcon = new ImageIcon("images/START.png");
        JLabel imageLabel = new JLabel(startIcon);
        gbc.gridy = 1;
        add(imageLabel, gbc);

        // Create buttons
        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        // Set buttons to equal size
        Dimension buttonSize = new Dimension(200, 50);
        easyButton.setPreferredSize(buttonSize);
        mediumButton.setPreferredSize(buttonSize);
        hardButton.setPreferredSize(buttonSize);

        gbc.gridy = 2;
        add(easyButton, gbc);
        gbc.gridy = 3;
        add(mediumButton, gbc);
        gbc.gridy = 4;
        add(hardButton, gbc);

        easyButton.addActionListener(e -> nextScreen(5));
        mediumButton.addActionListener(e -> nextScreen(2.5));
        hardButton.addActionListener(e -> nextScreen(2.3));
    }

    private void nextScreen(double difficulty) {
        mainFrame.setDifficulty(difficulty);
        mainFrame.setHighScoreScreen(new HighScoreScreen(mainFrame));
        mainFrame.showScreen("HighScoreScreen");
    }
}
