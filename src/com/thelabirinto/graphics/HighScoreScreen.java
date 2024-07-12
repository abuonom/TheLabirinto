package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HighScoreScreen extends JPanel {
    public HighScoreScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Area di testo per visualizzare i punteggi
        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        // Etichetta di uscita
        JLabel exitLabel = new JLabel("Premere il pulsante Avanti per proseguire", SwingConstants.CENTER);
        exitLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        add(exitLabel, BorderLayout.NORTH);

        // Pulsante Avanti
        JButton nextButton = new JButton("Avanti");
        nextButton.setFont(new Font("Serif", Font.PLAIN, 20));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedToNextScreen(mainFrame);
            }
        });

        // Pannello per il pulsante Avanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Carica i punteggi
        loadHighScores(scoreArea);
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database

        scoreArea.setText("Punteggio 1: 100\nPunteggio 2: 90\nPunteggio 3: 80\nPunteggio 4: 70\nPunteggio 5: 60");
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("NameInputScreen");
        mainFrame.getNameInputScreen().requestFocusInWindow();
    }
}