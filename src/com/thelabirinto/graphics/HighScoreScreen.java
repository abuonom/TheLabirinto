package com.thelabirinto.graphics;

import com.thelabirinto.builder.Player;
import com.thelabirinto.connection.DbConnectionSingleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HighScoreScreen extends JPanel {
    public HighScoreScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Pannello per i punteggi
        JPanel scorePanel = new JPanel(new GridLayout(10, 5));
        add(new JScrollPane(scorePanel), BorderLayout.CENTER);
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
        loadHighScores(scorePanel,mainFrame);
    }

    private void loadHighScores(JPanel scorePanel, MainFrame mainFrame) {
        try {
            DbConnectionSingleton dbConnection = mainFrame.getDbConnection();
            dbConnection.buildConnection();
            ArrayList<Player> topPlayers = dbConnection.getTopPlayersByDifficulty(mainFrame.getDifficulty());

            if (topPlayers.isEmpty()) {
                JLabel errorLabel = new JLabel("Per la difficoltà selezionata non ci sono punteggi");
                errorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
                scorePanel.add(errorLabel);
            } else {
                int i = 0;
                for (Player player : topPlayers) {
                    JLabel scoreLabel = new JLabel((i + 1) + ") " + player.getName() + " " + player.getSurname() + " - " + player.getMoves());
                    scoreLabel.setFont(new Font("Serif", Font.PLAIN, 20));
                    scorePanel.add(scoreLabel);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Funzioni online non disponibili, verificare connessione");
            errorLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            scorePanel.add(errorLabel);
        }
    }


    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("NameInputScreen");
    }
}
