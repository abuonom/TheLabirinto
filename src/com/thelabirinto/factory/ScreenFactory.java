package com.thelabirinto.factory;

import com.thelabirinto.graphics.*;
import javax.swing.*;

public class ScreenFactory {
    private final MainFrame mainFrame;

    public ScreenFactory(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public JPanel createScreen(ScreenType screenType) {
        return switch (screenType) {
            case HOME -> new HomeScreen(mainFrame);
            case NAME_INPUT -> new NameInputScreen(mainFrame);
            case HIGH_SCORE -> new HighScoreScreen(mainFrame);
            case MAZE -> new MazeScreen(mainFrame);
        };
    }
}
