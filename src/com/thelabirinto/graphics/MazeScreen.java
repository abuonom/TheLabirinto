package com.thelabirinto.graphics;

import com.thelabirinto.builder.Position;
import com.thelabirinto.factory.ScreenFactory;
import com.thelabirinto.factory.ScreenType;
import com.thelabirinto.strategy.MovementStrategy;
import com.thelabirinto.strategy.PlayerMovement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MazeScreen extends JPanel {
    MainFrame mainFrame;
    private Image floorImage;
    private Image wallImage;
    private Image robotImage;
    private Image exitImage;
    private final int mazeRows;
    private final int mazeCols;
    private int difficulty;
    private JLabel infoLabel; // Label per mostrare le informazioni sulle mosse
    private final String[] emojiArray = {"🕹️", "⭐", "🎲"};
    private final Set<Integer> pressedKeys = new HashSet<>();

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
        infoLabel = new JLabel("0" + "🕹️");
        infoLabel.setBounds(10, 10, 70, 40); // Posizione e dimensioni della label
        infoLabel.setOpaque(true);
        infoLabel.setBackground(new Color(0, 0, 0, 109)); // Sfondo trasparente
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20)); // Usa un font che supporta le emoji

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
                System.out.println(e);
                pressedKeys.add(e.getKeyCode());
                handleKeyPress();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

    private void handleKeyPress() {
        int dx = 0, dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_W)) {
            dx = -1;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            dx = 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            dy = -1;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            dy = 1;
        }

        if (dx != 0 || dy != 0) {
            handleMovement(dx, dy);
        }
    }

    private void handleMovement(int dx, int dy) {
        String emoji;
        Random random = new Random();
        int strategyChance = random.nextInt(100);
        if (strategyChance < 30) {
            emoji = emojiArray[0];
        } else if (strategyChance < 60) {
            emoji = emojiArray[1];
        } else {
            emoji = emojiArray[2];
        }
        PlayerMovement playerMovement = new PlayerMovement();
        playerMovement.setDirection(dx, dy);

        Position currentPos = mainFrame.getMaze().getRobotPosition();
        Position newPos = ((MovementStrategy) playerMovement).getNextPosition(mainFrame.getMaze(), currentPos);
        if (mainFrame.getMaze().isValidMove(newPos.getX(), newPos.getY())) {
            mainFrame.getMaze().updateRobot(newPos);
            mainFrame.getMaze().regenerateMap(mainFrame.getDifficulty());
            if (mainFrame.getMaze().getRobotPosition().equals(mainFrame.getMaze().getExitPosition())) {
                mainFrame.getDbConnection().insertPlayer(mainFrame.getMaze().getPlayer());
                proceedToNextScreen(mainFrame);
            }
            repaint();
            mainFrame.getMaze().getPlayer().addMoves();
            infoLabel.setText(mainFrame.getMaze().getPlayer().getMoves() + emoji);
        }
    }

    private void proceedToNextScreen(MainFrame mainFrame) {
        ScreenFactory screenFactory = new ScreenFactory(mainFrame);
        mainFrame.getMainPanel().add(screenFactory.createScreen(ScreenType.HIGH_SCORE), "WinScreen");
        mainFrame.showScreen("WinScreen");
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
