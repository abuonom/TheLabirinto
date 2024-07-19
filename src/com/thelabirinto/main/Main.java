package com.thelabirinto.main;

import com.thelabirinto.graphics.MainFrame;

import java.awt.*;

/**
 * Classe principale che avvia l'applicazione TheLabirinto.
 */
public class Main {
    /**
     * Metodo principale che avvia l'applicazione.
     *
     * @param args gli argomenti della riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Ottiene le dimensioni dello schermo
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        // Crea un'istanza di MainFrame con la larghezza e l'altezza dello schermo
        MainFrame mainFrame = new MainFrame((int) width, (int) height);

        // Imposta la visibilità del frame principale
        mainFrame.setVisible(true);
    }
}
