package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeScreen extends JPanel {
    private Image floorImage;
    private Image wallImage;
    private Image robotImage;
    private JLabel robotLabel;
    private Point robotPosition;

    private final int mazeRows;
    private final int mazeCols;
    private final int tileSize;

    // Matrice del labirinto (0 = pavimento, 1 = muro, 2 = robot)
    private final int[][] mazeMatrix = {
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 1, 1, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 2}
    };

    public MazeScreen(MainFrame mainFrame) {
        this.tileSize = 64;  // TILE_SIZE is fixed
        this.mazeRows = MainFrame.SCREEN_HEIGHT / tileSize;
        this.mazeCols = MainFrame.SCREEN_WIDTH / tileSize;

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
        layeredPane.setPreferredSize(new Dimension(mazeCols * tileSize, mazeRows * tileSize));

        JPanel mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };
        mazePanel.setBounds(0, 0, mazeCols * tileSize, mazeRows * tileSize);
        layeredPane.add(mazePanel, JLayeredPane.DEFAULT_LAYER);

        robotLabel = new JLabel(new ImageIcon(robotImage));
        robotPosition = findRobotPosition();
        robotLabel.setBounds(robotPosition.x * tileSize, robotPosition.y * tileSize, tileSize, tileSize);
        layeredPane.add(robotLabel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < mazeRows; y++) {
            for (int x = 0; x < mazeCols; x++) {
                Image image = null;
                if (mazeMatrix[y][x] == 0) {
                    image = floorImage;
                } else if (mazeMatrix[y][x] == 1) {
                    image = wallImage;
                }
                if (image != null) {
                    g.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, this);
                }
            }
        }
    }

    private Point findRobotPosition() {
        for (int y = 0; y < mazeRows; y++) {
            for (int x = 0; x < mazeCols; x++) {
                if (mazeMatrix[y][x] == 2) {
                    return new Point(x, y);
                }
            }
        }
        return new Point(0, 0); // Default position if not found
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
        int newX = robotPosition.x + dx;
        int newY = robotPosition.y + dy;

        if (newX >= 0 && newX < mazeCols && newY >= 0 && newY < mazeRows) {
            if (mazeMatrix[newY][newX] != 1) { // Check if the new position is not a wall
                mazeMatrix[robotPosition.y][robotPosition.x] = 0; // Clear the old robot position
                robotPosition.setLocation(newX, newY);
                mazeMatrix[newY][newX] = 2; // Set the new robot position
                robotLabel.setLocation(newX * tileSize, newY * tileSize);
                repaint();
            }
        }
    }
}
