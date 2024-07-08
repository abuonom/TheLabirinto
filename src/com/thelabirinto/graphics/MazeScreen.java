package com.thelabirinto.graphics;

import com.thelabirinto.builder.Position;
import com.thelabirinto.strategy.MovementStrategy;
import com.thelabirinto.strategy.PlayerMovement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class MazeScreen extends JPanel {
    MainFrame mainFrame;
    private Image floorImage;
    private Image wallImage;
    private Image robotImage;
    private Image exitImage;
    private final int mazeRows;
    private final int mazeCols;
    private final PlayerMovement playerMovement;
    private int difficulty;

    public MazeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.mazeRows = mainFrame.getMaze().getWindowHeight() / mainFrame.getMaze().getTileSize();
        this.mazeCols = mainFrame.getMaze().getWindowWidth() / mainFrame.getMaze().getTileSize();
        this.playerMovement = new PlayerMovement();

        System.out.println(mainFrame.getMaze());
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
        for (int x = 0; x < mazeRows; x++) {
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
                    case KeyEvent.VK_W -> dy = -1;
                    case KeyEvent.VK_S -> dy = 1;
                    case KeyEvent.VK_A -> dx = -1;
                    case KeyEvent.VK_D -> dx = 1;
                }
                playerMovement.setDirection(dx, dy);
                System.out.println(mainFrame.getMaze());
                handleMovement();
            }
        });
    }

    private void handleMovement() {
        MovementStrategy movementStrategy;
        Random random = new Random();
        int strategyChance = random.nextInt(100);

        movementStrategy = playerMovement;

        Position currentPos = mainFrame.getMaze().getRobotPosition();
        Position newPos = movementStrategy.getNextPosition(mainFrame.getMaze(), currentPos);

        // Debugging output to trace the values
        System.out.println("Current Position: " + currentPos);
        System.out.println("New Position: " + newPos);

        if (mainFrame.getMaze().isValidMove(newPos.getX(), newPos.getY())) {
            System.out.println("Move is valid. Updating robot position.");
            mainFrame.getMaze().updateRobot(newPos);
            repaint();
        } else {
            System.out.println("Move is invalid.");
        }
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        mainFrame.showScreen("HighScoreScreen");
        mainFrame.getHighScoreScreen().requestFocusInWindow();
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
