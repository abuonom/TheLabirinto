package com.thelabirinto.graphics;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Schermata per l'inserimento del nome e cognome da parte dell'utente
 */
public class NameInputScreen extends JPanel {
    private final JTextField nameField;
    private final JTextField surnameField;

    /**
     * Costruttore
     * Si occupa anche di preparare i componenti della schermata
     * @param mainFrame l'istanza di MainFrame per passare alla schermata successiva
     */
    public NameInputScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Campi di testo
        nameField = createRestrictedTextField(15);
        surnameField = createRestrictedTextField(15);
        JLabel nameLabel = new JLabel("Nome:");
        JLabel surnameLabel = new JLabel("Cognome:");

        // Pannello di input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(surnameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(surnameField, gbc);

        // Pulsante di avvio partita
        JButton startButton = new JButton("Avvia Partita");
        startButton.setFont(new Font("Serif", Font.PLAIN, 20));
        startButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (!name.isEmpty() && !surname.isEmpty()) {
                mainFrame.createMaze(name, surname, mainFrame.getDifficulty());
                proceedToNextScreen(mainFrame);
            } else {
                JOptionPane.showMessageDialog(NameInputScreen.this,
                        "Per favore, inserisci sia il nome che il cognome.",
                        "Errore di Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Pannello per il pulsante
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Crea un campo di testo con restrizioni per consentire solo lettere.
     * @param columns il numero di colonne del campo di testo
     * @return un JTextField con restrizioni di input
     */
    private JTextField createRestrictedTextField(int columns) {
        JTextField textField = new JTextField(columns);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                if (string.matches("[a-zA-Z]+")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                if (text.matches("[a-zA-Z]+")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
        return textField;
    }

    /**
     * Passa alla schermata successiva.
     * @param mainFrame l'istanza di MainFrame per mostrare la schermata del labirinto
     */
    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("MazeScreen");
        mainFrame.makeFocus();
    }
}
