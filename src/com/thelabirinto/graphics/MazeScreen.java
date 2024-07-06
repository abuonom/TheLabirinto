package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeScreen extends JPanel {
    MainFrame mainFrame;
    private Image floorImage;
    private Image wallImage;
    private Image robotImage;
    private JLabel robotLabel;
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

        robotLabel = new JLabel(new ImageIcon(robotImage));
        robotLabel.setBounds(mainFrame.getMaze().getRobotPosition().getX() * mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getRobotPosition().getY() * mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize());
        layeredPane.add(robotLabel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane, BorderLayout.CENTER);
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < mazeRows; y++) {
            for (int x = 0; x < mazeCols; x++) {
                Image image = null;
                if (mainFrame.getMaze().getMap()[y][x] == 0) {
                    image = floorImage;
                } else if (mainFrame.getMaze().getMap()[y][x] == 1) {
                    image = wallImage;
                }
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
                    case KeyEvent.VK_UP:
                        dy = -1;
                        break;
                    case KeyEvent.VK_DOWN:
                        dy = 1;
                        break;
                    case KeyEvent.VK_LEFT:
                        dx = -1;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 1;
                        break;
                }

                moveRobot(dx, dy);
            }
        });
    }

    private void moveRobot(int dx, int dy) {
        int newX = mainFrame.getMaze().getRobotPosition().getX() + dx;
        int newY = mainFrame.getMaze().getRobotPosition().getY() + dy;

        if (newX >= 0 && newX < mazeCols && newY >= 0 && newY < mazeRows) {
            if (mainFrame.getMaze().getMap()[newY][newX] != 1) { // Check if the new position is not a wall
                mainFrame.getMaze().getMap()[mainFrame.getMaze().getRobotPosition().getY()][mainFrame.getMaze().getRobotPosition().getX()] = 0; // Clear the old robot position
                mainFrame.getMaze().getRobotPosition().setLocation(newX, newY);
                mainFrame.getMaze().getMap()[newY][newX] = 2; // Set the new robot position
                robotLabel.setLocation(newX * mainFrame.getMaze().getTileSize(), newY * mainFrame.getMaze().getTileSize());
                repaint();
            }
        }
    }
}
