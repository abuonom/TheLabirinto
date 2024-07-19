package com.thelabirinto.graphics;

import com.thelabirinto.builder.Position;
import com.thelabirinto.factory.ScreenFactory;
import com.thelabirinto.factory.ScreenType;
import com.thelabirinto.strategy.AStarMovementStrategy;
import com.thelabirinto.strategy.MovementStrategy;
import com.thelabirinto.strategy.PlayerMovementStrategy;
import com.thelabirinto.strategy.RandomMovementStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
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
    private JLabel infoLabel; // Label per mostrare le informazioni sulle mosse
    private final String[] emojiArray = {"🕹️", "🎲", "⭐"};
    private final Set<Integer> pressedKeys = new HashSet<>();

    public MazeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.mazeRows = mainFrame.getMaze().getWindowHeight() / mainFrame.getMaze().getTileSize();
        this.mazeCols = mainFrame.getMaze().getWindowWidth() / mainFrame.getMaze().getTileSize();
        if (!loadImages()) {
            showErrorAndExit();
        } else {
            initializeLayout();
            setupKeyBindings();
        }
    }

    private boolean loadImages() {
        String[] imagePaths = {
                "src/com/thelabirinto/resources/image/BACK.png",
                "src/com/thelabirinto/resources/image/WALL.png",
                "src/com/thelabirinto/resources/image/RIGHT.png",
                "src/com/thelabirinto/resources/image/EXIT_YES.png"
        };

        for (String path : imagePaths) {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                System.err.println("File non trovato: " + path);
                return false;
            }
        }

        try {
            floorImage = new ImageIcon(imagePaths[0]).getImage();
            wallImage = new ImageIcon(imagePaths[1]).getImage();
            robotImage = new ImageIcon(imagePaths[2]).getImage();
            exitImage = new ImageIcon(imagePaths[3]).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private void showErrorAndExit() {
        setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel("Errore con le texture", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());  // Aggiunge spazio verticale prima della label
        panel.add(errorLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));  // Aggiunge spazio tra la label e il pulsante
        panel.add(closeButton);
        panel.add(Box.createVerticalGlue());  // Aggiunge spazio verticale dopo il pulsante

        add(panel, BorderLayout.CENTER);
    }


    private void initializeLayout() {
        setLayout(new BorderLayout());
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(mazeCols * mainFrame.getMaze().getTileSize() /3,
                mazeRows * mainFrame.getMaze().getTileSize() / 3));

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

        // Verificare se sono premuti i tasti per il movimento
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            dx -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            dx += 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            dy -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            dy += 1;
        }

        // Se c'è movimento, aggiornare la posizione e incrementare il contatore delle mosse una sola volta
        if (dx != 0 || dy != 0) {
            handleMovement(dx, dy);
        }
    }

    private void handleMovement(int dx, int dy) {
        String emoji;
        Random random = new Random();
        int strategyChance = random.nextInt(100);
        MovementStrategy movementStrategy;
        if (strategyChance < 30) {
            movementStrategy = new PlayerMovementStrategy(dx,dy);
            emoji = emojiArray[0];
        } else if (strategyChance < 60) {
            movementStrategy = new RandomMovementStrategy();
            emoji = emojiArray[1];
        } else {
            movementStrategy = new AStarMovementStrategy();
            emoji = emojiArray[2];
            }
        Position currentPos = mainFrame.getMaze().getRobotPosition();
        Position newPos = movementStrategy.getNextPosition(mainFrame.getMaze(), currentPos);

        if (mainFrame.getMaze().isValidMove(newPos.getX(), newPos.getY())) {
            mainFrame.getMaze().updateRobot(newPos);
            mainFrame.getMaze().regenerateMap(mainFrame.getDifficulty());
            if (mainFrame.getMaze().getRobotPosition().equals(mainFrame.getMaze().getExitPosition())) {
                try {
                    mainFrame.getDbConnection().insertPlayer(mainFrame.getMaze().getPlayer());
                } catch (Exception e) {
                    e.printStackTrace();
                    proceedToNextScreen(mainFrame);
                }
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
}
