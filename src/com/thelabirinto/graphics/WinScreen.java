package com.thelabirinto.graphics;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinScreen extends JPanel {
    private final JTextArea scoreArea;

    public WinScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Pannello superiore per messaggio di vittoria
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JLabel winLabel = new JLabel("HAI VINTO!", SwingConstants.CENTER);
        winLabel.setFont(new Font("Serif", Font.BOLD, 30));
        topPanel.add(winLabel, BorderLayout.NORTH);

        // Etichetta per nome, cognome e numero di mosse
        JLabel playerInfoLabel = new JLabel(mainFrame.getMaze().getPlayer().getName() + " " + mainFrame.getMaze().getPlayer().getSurname() + " - Numero di mosse: " + mainFrame.getMaze().getPlayer().getMoves(), SwingConstants.CENTER);
        playerInfoLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        topPanel.add(playerInfoLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Area di testo per visualizzare i punteggi
        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        add(new JScrollPane(scoreArea), BorderLayout.CENTER);

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

        // Imposta il focus
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadHighScores(scoreArea);
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
        scoreArea.setText("Punteggio 1: 100\nPunteggio 2: 90\nPunteggio 3: 80\nPunteggio 4: 70\nPunteggio 5: 60");
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("HomeScreen");
        mainFrame.getHomeScreen().requestFocusInWindow();
    }
}
