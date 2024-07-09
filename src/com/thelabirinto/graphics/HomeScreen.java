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

        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        gbc.gridy = 1;
        add(easyButton, gbc);
        gbc.gridy = 2;
        add(mediumButton, gbc);
        gbc.gridy = 3;
        add(hardButton, gbc);

        easyButton.addActionListener(e -> nextScreen(5));
        mediumButton.addActionListener(e -> nextScreen((int) Math.ceil(2.5)));
        hardButton.addActionListener(e -> nextScreen((int) Math.ceil(2.3)));
    }

    private void nextScreen(double difficulty) {
        mainFrame.setDifficulty(difficulty);
        mainFrame.showScreen("HighScoreScreen");
    }
}
