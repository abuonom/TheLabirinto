package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;


public class NameInputScreen extends JPanel {
    private final JTextField nameField;
    private final JTextField surnameField;

    public NameInputScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Crea i campi di testo per nome e cognome
        nameField = new JTextField(15);
        surnameField = new JTextField(15);

        // Etichette per i campi di testo
        JLabel nameLabel = new JLabel("Nome:");
        JLabel surnameLabel = new JLabel("Cognome:");

        // Pannello per i campi di ingresso
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(surnameLabel);
        inputPanel.add(surnameField);

        // Crea il pulsante di avvio
        JButton startButton = new JButton("Avvia Partita");
        startButton.setFont(new Font("Serif", Font.PLAIN, 20));

        // Aggiungi l'ActionListener al pulsante
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (!name.isEmpty() && !surname.isEmpty()) {
                mainFrame.createMaze(name,surname,mainFrame.getDifficulty());
                proceedToNextScreen(mainFrame);
            } else {
                JOptionPane.showMessageDialog(NameInputScreen.this,
                        "Per favore, inserisci sia il nome che il cognome.",
                        "Errore di Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Pannello per il pulsante di avvio
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        // Aggiungi i pannelli al JPanel principale
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setFocusable(true);
        requestFocusInWindow();
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("MazeScreen");
        mainFrame.makeFocus();
    }
}
