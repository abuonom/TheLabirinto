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
    private MovementStrategy movementStrategy;
    private int difficulty;
    private int moveCount; // Contatore delle mosse
    private JLabel infoLabel; // Label per mostrare le informazioni sulle mosse

    public MazeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.mazeRows = mainFrame.getMaze().getWindowHeight() / mainFrame.getMaze().getTileSize();
        this.mazeCols = mainFrame.getMaze().getWindowWidth() / mainFrame.getMaze().getTileSize();
        this.moveCount = 0; // Inizializza il contatore delle mosse
        loadImages();
        initializeLayout();
        setupKeyBindings();
    }

    private void loadImages() {
        floorImage = new ImageIcon("images/BACK.png").getImage();
        wallImage = new ImageIcon("images/WALL.png").getImage();
        robotImage = new ImageIcon("images/RIGHT.png").getImage();
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

        // Creazione della label informativa
        infoLabel = new JLabel("0\n");
        infoLabel.setBounds(10, 10, 50, 50); // Posizione e dimensioni della label
        infoLabel.setOpaque(true);
        infoLabel.setBackground(new Color(0, 0, 0, 128)); // Sfondo trasparente
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Aumenta la dimensione del font

        layeredPane.add(infoLabel, JLayeredPane.PALETTE_LAYER);

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
                    g.drawImage(image, y * mainFrame.getMaze().getTileSize(), x * mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize(), mainFrame.getMaze().getTileSize(), this);
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
                    case KeyEvent.VK_W -> dx = -1;
                    case KeyEvent.VK_S -> dx = 1;
                    case KeyEvent.VK_A -> dy = -1;
                    case KeyEvent.VK_D -> dy = 1;
                    default -> {
                        return;
                    }
                }
                System.out.println(mainFrame.getMaze());
                handleMovement(dx, dy);
            }
        });
    }

    private void handleMovement(int dx, int dy) {
        Random random = new Random();
        int strategyChance = random.nextInt(100);
        PlayerMovement playerMovement = new PlayerMovement();
        playerMovement.setDirection(dx, dy);
        movementStrategy = playerMovement;

        Position currentPos = mainFrame.getMaze().getRobotPosition();
        Position newPos = movementStrategy.getNextPosition(mainFrame.getMaze(), currentPos);

        // Debugging output to trace the values
        System.out.println("Current Position: " + currentPos);
        System.out.println("New Position: " + newPos);

        if (mainFrame.getMaze().isValidMove(newPos.getX(), newPos.getY())) {
            mainFrame.getMaze().updateRobot(newPos);
            mainFrame.getMaze().regenerateMap(mainFrame.getDifficulty());
            if (mainFrame.getMaze().getRobotPosition().equals(mainFrame.getMaze().getExitPosition()))
                proceedToNextScreen(mainFrame);
            repaint();
            moveCount++;
            updateInfoLabel();
        }
    }

    private void updateInfoLabel() {
        infoLabel.setText(moveCount + "");
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        WinScreen winScreen = new WinScreen(mainFrame);
        mainFrame.getMainPanel().add(winScreen, "WinScreen");
        mainFrame.showScreen("WinScreen");
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
