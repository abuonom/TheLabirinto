package com.thelabirinto.graphics;

import com.thelabirinto.builder.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeScreen extends JPanel {
    MainFrame mainFrame;
    private Image floorImage;
    private Image wallImage;
    private Image robotImage;
    private Image exitImage;
    private final int mazeRows;
    private final int mazeCols;

    public MazeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.mazeRows = mainFrame.getMaze().getWindowHeight() / mainFrame.getMaze().getTileSize();
        this.mazeCols = mainFrame.getMaze().getWindowWidth() / mainFrame.getMaze().getTileSize();

        loadImages();
        initializeLayout();
        setupKeyBindings();
    }

    private void loadImages() {
        floorImage = new ImageIcon("images/BACK.png").getImage();
        wallImage = new ImageIcon("images/WALL.png").getImage();
        robotImage = new ImageIcon("images/START.png").getImage();
        exitImage = new ImageIcon("images/EXIT_YES.png").getImage();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(mazeCols * mainFrame.getMaze().getTileSize(),
                mazeRows * mainFrame.getMaze().getTileSize()));

        JPanel mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };
        mazePanel.setBounds(0, 0, mazeCols * mainFrame.getMaze().getTileSize(),
                mazeRows * mainFrame.getMaze().getTileSize());
        layeredPane.add(mazePanel, JLayeredPane.DEFAULT_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private void drawMaze(Graphics g) {
        for (int x= 0; x < mazeRows; x++) {
            for (int y = 0; y < mazeCols; y++) {
                Image image = switch (mainFrame.getMaze().getMap()[x][y]) {
                    case 0 -> floorImage;
                    case 1 -> wallImage;
                    case 2 -> exitImage;
                    case 3 -> robotImage;
                    default -> null;
                };
                if (image != null) {
                    g.drawImage(image, x * mainFrame.getMaze().getTileSize(), y * mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize(), this);
                }
            }
        }
    }

    private void setupKeyBindings() {
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                int dx = 0, dy = 0;

                switch (key) {
                    case KeyEvent.VK_UP -> dy = -1;
                    case KeyEvent.VK_DOWN -> dy = 1;
                    case KeyEvent.VK_LEFT -> dx = -1;
                    case KeyEvent.VK_RIGHT -> dx = 1;
                }
                moveRobot(dx, dy);
            }
        });
    }

    private void moveRobot(int dx, int dy) {
        Position newRobotPosition = mainFrame.getMaze().getRobotPosition();

        int newX = newRobotPosition.getX() + dx;
        int newY = newRobotPosition.getY() + dy;

        // Verifica se la nuova posizione è valida
        if (isValidMove(newX, newY)) {
            if(mainFrame.getMaze().getMap()[newX][newY] == 2)
                proceedToNextScreen(mainFrame);
            newRobotPosition  = new Position(newX, newY);
            mainFrame.getMaze().updateRobot(newRobotPosition);
            repaint();
        }
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("HighScoreScreen");
        mainFrame.getHighScoreScreen().requestFocusInWindow();
    }

    // Metodo ausiliario per verificare se una mossa è valida
    private boolean isValidMove(int x, int y) {
        // Verifica se le coordinate sono all'interno dei limiti della mappa
        if (x < 0 || x >= mainFrame.getMaze().getMap().length || y < 0 || y >= mainFrame.getMaze().getMap()[0].length) {
            return false;
        }

        // Verifica se la casella sulla mappa è attraversabile (valore 0)
        return (mainFrame.getMaze().getMap()[x][y] == 0 || mainFrame.getMaze().getMap()[x][y] == 2);
    }


}
