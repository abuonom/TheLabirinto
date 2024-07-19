package com.thelabirinto.factory;

import com.thelabirinto.graphics.*;
import javax.swing.*;

/**
 * Classe costruita seguendo il Pattern Factory
 * Crea degli oggetti JPanel in base al valore enum ricevuto
 */
public class ScreenFactory{
    private final MainFrame mainFrame;

    /**
     * Costruttore
     * @param mainFrame viene passato alle varie schermate come parametro
     */
    public ScreenFactory(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Si occupa di creare un oggetto JPanel del tipo suggerito da ScreenType
     * @param screenType
     * @return un oggetto JPanel con la schermata richiesta
     */
    public JPanel createScreen(ScreenType screenType) {
        return switch (screenType) {
            case HOME -> new HomeScreen(mainFrame);
            case NAME_INPUT -> new NameInputScreen(mainFrame);
            case HIGH_SCORE -> new HighScoreScreen(mainFrame);
            case MAZE -> new MazeScreen(mainFrame);
        };
    }
}
