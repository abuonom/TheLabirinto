package com.thelabirinto.graphics;

import com.thelabirinto.builder.Player;
import com.thelabirinto.connection.DbConnectionSingleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Classe che rappresenta la schermata di visualizzazione dei punteggi
 */
public class HighScoreScreen extends JPanel {
    /**
     * Costruttore, si occupa anche di creare il pannello con i punteggi
     * @param mainFrame contiene l'istanza del DbConnection e permette di invocare showScreen
     */
    public HighScoreScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Pannello per i punteggi
        JPanel scorePanel = new JPanel(new GridLayout(10, 5));
        add(new JScrollPane(scorePanel), BorderLayout.CENTER);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();

        // Pulsante Avanti
        JButton nextButton = new JButton("Avanti");
        nextButton.setFont(new Font("Serif", Font.PLAIN, 20));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedToNextScreen(mainFrame);
            }
        });
        buttonPanel.add(nextButton);

        // Pulsante Seleziona Difficoltà
        JButton difficultyButton = new JButton("Seleziona Difficoltà");
        difficultyButton.setFont(new Font("Serif", Font.PLAIN, 20));
        difficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToDifficultySelection(mainFrame);
            }
        });
        buttonPanel.add(difficultyButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Carica i punteggi
        loadHighScores(scorePanel, mainFrame);
    }

    /**
     * Si occupa di eseguire l'operazione di CRUD richiedendo mediante SQL la lista dei
     * 50 migliori punteggi per la difficoltà scelta a inizio gioco
     * @param scorePanel
     * @param mainFrame
     */
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

    /**
     * Procede alla schermata successiva
     * @param mainFrame
     */
    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("NameInputScreen");
    }

    /**
     * Ritorna alla schermata di selezione della difficoltà
     * @param mainFrame
     */
    private void returnToDifficultySelection(MainFrame mainFrame) {
        mainFrame.showScreen("HomeScreen");
    }
}
