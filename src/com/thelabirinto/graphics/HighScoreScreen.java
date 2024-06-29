package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HighScoreScreen extends JPanel {
    public HighScoreScreen(MainFrame mainFrame){
        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN,20));
        add(new JScrollPane(scoreArea),BorderLayout.CENTER);
        JLabel exitLabel = new JLabel("Premere SPACE per proseguire",SwingConstants.CENTER);
        exitLabel.setFont(new Font("Serif", Font.PLAIN,20));
        add(exitLabel, BorderLayout.SOUTH);
        loadHighScores(scoreArea);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    mainFrame.showScreen("NameInputScreen");
                    mainFrame.getHomeScreen().requestFocusInWindow();
                }
            }
        });
    }
        private void loadHighScores(JTextArea scoreArea) {
            // Implementazione per caricare i punteggi dal database
            scoreArea.setText("Punteggio 1: 100\nPunteggio 2: 90\nPunteggio 3: 80\nPunteggio 4: 70\nPunteggio 5: 60");
        }
}
