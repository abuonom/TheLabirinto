package com.thelabirinto.main;

import com.thelabirinto.graphics.MainFrame;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        MainFrame mainFrame = new MainFrame((int) width, (int) height);
        mainFrame.setVisible(true);
    }
}
