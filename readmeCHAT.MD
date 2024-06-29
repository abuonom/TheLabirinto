Il tuo progetto per l'algoritmo di cammino del robot in un labirinto è ben strutturato e suddiviso in fasi chiare. Di seguito, ti fornirò una guida dettagliata per ogni fase del progetto, insieme a un'implementazione schematica in Java utilizzando Swing per l'interfaccia grafica e JDBC per la gestione del database. Questo ti aiuterà a creare un progetto modulare, estensibile e manutenibile.

### Fase 1: HOME iniziale

1. **Creare una finestra iniziale con una scritta "TheLabirinto" e "Premere SPACE per iniziare"**

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HomeScreen extends JFrame {
    public HomeScreen() {
        setTitle("TheLabirinto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("TheLabirinto", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        panel.add(titleLabel, BorderLayout.CENTER);

        JLabel startLabel = new JLabel("Premere SPACE per iniziare", SwingConstants.CENTER);
        startLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(startLabel, BorderLayout.SOUTH);

        add(panel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showHighScores();
                }
            }
        });
    }

    private void showHighScores() {
        // Implementazione per mostrare i 5 migliori punteggi
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeScreen homeScreen = new HomeScreen();
            homeScreen.setVisible(true);
        });
    }
}
```

### Fase 2: Mostrare i migliori punteggi

1. **Mostrare i 5 migliori punteggi, presi da un database, e una scritta "Premere SPACE per proseguire"**

```java
public class HighScoreScreen extends JFrame {
    public HighScoreScreen() {
        setTitle("High Scores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        JLabel proceedLabel = new JLabel("Premere SPACE per proseguire", SwingConstants.CENTER);
        proceedLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(proceedLabel, BorderLayout.SOUTH);

        add(panel);

        loadHighScores(scoreArea);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showNameInput();
                }
            }
        });
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
    }

    private void showNameInput() {
        // Implementazione per mostrare il pannello di inserimento nome e cognome
    }
}
```

### Fase 3: Inserimento nome e cognome

1. **Fornire all'utente un campo di input per inserire nome e cognome e un pulsante per proseguire**

```java
public class NameInputScreen extends JFrame {
    public NameInputScreen() {
        setTitle("Inserisci Nome e Cognome");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JLabel("Nome:"));
        panel.add(nameField);

        JTextField surnameField = new JTextField();
        surnameField.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JLabel("Cognome:"));
        panel.add(surnameField);

        JButton proceedButton = new JButton("Premere ENTER per proseguire");
        proceedButton.setFont(new Font("Serif", Font.PLAIN, 20));
        proceedButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            if (!name.isEmpty() && !surname.isEmpty()) {
                dispose();
                generateMaze();
            }
        });
        panel.add(proceedButton);

        add(panel);
    }

    private void generateMaze() {
        // Implementazione per generare il labirinto
    }
}
```

### Fase 4: Generazione della matrice

1. **Creare la matrice del labirinto con muri e pavimenti casuali, assicurandosi che sia giocabile**

```java
public class Maze {
    private int[][] grid;
    private int width;
    private int height;
    private Position robotPosition;
    private Position exitPosition;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[width][height];
        generateMaze();
    }

    private void generateMaze() {
        // Generazione casuale della matrice con muri e pavimenti
    }

    private boolean isPlayable() {
        // Implementazione dell'algoritmo flood-fill per verificare se la mappa è giocabile
    }

    public void regenerateMaze() {
        do {
            generateMaze();
        } while (!isPlayable());
    }
}

class Position {
    int x, y;
    // Costruttori, getter, setter
}
```

### Fase 5: Eseguire flood-fill

1. **Eseguire l'algoritmo flood-fill per verificare la giocabilità della mappa**

```java
private boolean isPlayable() {
    boolean[][] visited = new boolean[width][height];
    return floodFill(robotPosition.x, robotPosition.y, visited);
}

private boolean floodFill(int x, int y, boolean[][] visited) {
    if (x < 0 || y < 0 || x >= width || y >= height || grid[x][y] == 1 || visited[x][y]) {
        return false;
    }
    if (new Position(x, y).equals(exitPosition)) {
        return true;
    }
    visited[x][y] = true;
    return floodFill(x + 1, y, visited) ||
           floodFill(x - 1, y, visited) ||
           floodFill(x, y + 1, visited) ||
           floodFill(x, y - 1, visited);
}
```

### Fase 6: Produzione della mappa

1. **Visualizzare la mappa utilizzando `Frame` e `Panel`**

```java
public class MazeScreen extends JFrame {
    private Maze maze;
    private int moveCount = 0;

    public MazeScreen(Maze maze) {
        this.maze = maze;
        setTitle("Maze");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MazePanel mazePanel = new MazePanel(maze);
        add(mazePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // Eseguire il movimento
                    moveCount++;
                    mazePanel.repaint();
                }
            }
        });
    }
}

class MazePanel extends JPanel {
    private Maze maze;

    public MazePanel(Maze maze) {
        this.maze = maze;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegnare la matrice del labirinto
    }
}
```

### Fase 7: Eseguire i movimenti del robot

1. **Implementare i movimenti del robot ad ogni pressione della barra spaziatrice**

```java
public class Robot {
    private MovementStrategy movementStrategy;
    private Position position;

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public void move(Maze maze) {
        movementStrategy.move(this, maze);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

public interface MovementStrategy {
    void move(Robot robot, Maze maze);
}

public class RandomMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione del movimento casuale
    }
}

public class PlayerMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione del movimento deciso dal giocatore
    }
}

public class AStarMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione dell'algoritmo A*
    }
}
```

### Fase 8: Gestione delle mosse

1.

**Aggiungere un contatore di mosse sullo schermo**

```java
class MazePanel extends JPanel {
    private Maze maze;
    private int moveCount = 0;

    public MazePanel(Maze maze) {
        this.maze = maze;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegnare la matrice del labirinto

        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("Mosse: " + moveCount, 10, 20);
    }

    public void incrementMoveCount() {
        moveCount++;
    }
}
```

### Fase 9: Aggiornamento del database

1. **Aggiornare il database con il punteggio del giocatore**

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/labirinto";
    private static final String USER = "root";
    private static final String PASS = "password";

    public void saveScore(String name, String surname, int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO scores (name, surname, score) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, surname);
                pstmt.setInt(3, score);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### Fase 10: Schermata finale

1. **Mostrare di nuovo la schermata con i punteggi e una scritta "Premere SPACE per uscire"**

```java
public class FinalScreen extends JFrame {
    public FinalScreen() {
        setTitle("High Scores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        JLabel exitLabel = new JLabel("Premere SPACE per uscire", SwingConstants.CENTER);
        exitLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(exitLabel, BorderLayout.SOUTH);

        add(panel);

        loadHighScores(scoreArea);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showHomeScreen();
                }
            }
        });
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
    }

    private void showHomeScreen() {
        // Implementazione per mostrare la schermata iniziale
    }
}
```

Con questa struttura modulare, puoi sviluppare il tuo progetto in modo incrementale, testando ogni fase man mano che procedi. Assicurati di gestire le eccezioni e di documentare il codice con commenti e Javadoc per una migliore manutenibilità. Buon lavoro con il tuo progetto!

Certamente! Di seguito, troverai l'implementazione del `main` del progetto e la gestione dei due pattern: Strategy e Builder.

### Main del progetto

Il `main` del progetto coordina le varie fasi, avviando l'interfaccia grafica e gestendo la logica principale.

```java
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeScreen homeScreen = new HomeScreen();
            homeScreen.setVisible(true);
        });
    }
}
```

### Gestione del Pattern Strategy

Il Pattern Strategy viene utilizzato per le diverse strategie di movimento del robot. Implementiamo un'interfaccia `MovementStrategy` e le relative classi concrete.

```java
public interface MovementStrategy {
    void move(Robot robot, Maze maze);
}

public class RandomMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione del movimento casuale
    }
}

public class PlayerMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione del movimento deciso dal giocatore
    }
}

public class AStarMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Maze maze) {
        // Implementazione dell'algoritmo A*
    }
}

public class Robot {
    private MovementStrategy movementStrategy;
    private Position position;

    public void setMovementStrategy(MovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public void move(Maze maze) {
        movementStrategy.move(this, maze);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
```

### Gestione del Pattern Builder

Il Pattern Builder viene utilizzato per costruire la configurazione del labirinto.

```java
public class Maze {
    private int[][] grid;
    private int width;
    private int height;
    private Position robotPosition;
    private Position exitPosition;

    private Maze(int width, int height, Position robotPosition, Position exitPosition, int[][] grid) {
        this.width = width;
        this.height = height;
        this.robotPosition = robotPosition;
        this.exitPosition = exitPosition;
        this.grid = grid;
    }

    // Metodi per ottenere le proprietà e gestire il labirinto

    public static class Builder {
        private int width;
        private int height;
        private Position robotPosition;
        private Position exitPosition;
        private int[][] grid;

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setRobotPosition(Position robotPosition) {
            this.robotPosition = robotPosition;
            return this;
        }

        public Builder setExitPosition(Position exitPosition) {
            this.exitPosition = exitPosition;
            return this;
        }

        public Builder setGrid(int[][] grid) {
            this.grid = grid;
            return this;
        }

        public Maze build() {
            return new Maze(width, height, robotPosition, exitPosition, grid);
        }
    }
}

class Position {
    int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Override equals() e hashCode() per confrontare le posizioni
}
```

### Implementazione delle schermate con Swing

Di seguito, trovi le schermate principali, la gestione dei punteggi e l'interfaccia per l'inserimento del nome.

#### Schermata iniziale

```java
public class HomeScreen extends JFrame {
    public HomeScreen() {
        setTitle("TheLabirinto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("TheLabirinto", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        panel.add(titleLabel, BorderLayout.CENTER);

        JLabel startLabel = new JLabel("Premere SPACE per iniziare", SwingConstants.CENTER);
        startLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(startLabel, BorderLayout.SOUTH);

        add(panel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showHighScores();
                }
            }
        });
    }

    private void showHighScores() {
        HighScoreScreen highScoreScreen = new HighScoreScreen();
        highScoreScreen.setVisible(true);
    }
}
```

#### Schermata dei punteggi

```java
public class HighScoreScreen extends JFrame {
    public HighScoreScreen() {
        setTitle("High Scores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        JLabel proceedLabel = new JLabel("Premere SPACE per proseguire", SwingConstants.CENTER);
        proceedLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(proceedLabel, BorderLayout.SOUTH);

        add(panel);

        loadHighScores(scoreArea);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showNameInput();
                }
            }
        });
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
    }

    private void showNameInput() {
        NameInputScreen nameInputScreen = new NameInputScreen();
        nameInputScreen.setVisible(true);
    }
}
```

#### Schermata di input del nome

```java
public class NameInputScreen extends JFrame {
    public NameInputScreen() {
        setTitle("Inserisci Nome e Cognome");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JLabel("Nome:"));
        panel.add(nameField);

        JTextField surnameField = new JTextField();
        surnameField.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JLabel("Cognome:"));
        panel.add(surnameField);

        JButton proceedButton = new JButton("Premere ENTER per proseguire");
        proceedButton.setFont(new Font("Serif", Font.PLAIN, 20));
        proceedButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            if (!name.isEmpty() && !surname.isEmpty()) {
                dispose();
                generateMaze(name, surname);
            }
        });
        panel.add(proceedButton);

        add(panel);
    }

    private void generateMaze(String name, String surname) {
        // Implementazione per generare il labirinto e iniziare il gioco
    }
}
```

#### Schermata del labirinto

```java
public class MazeScreen extends JFrame {
    private Maze maze;
    private Robot robot;
    private int moveCount = 0;

    public MazeScreen(Maze maze, Robot robot) {
        this.maze = maze;
        this.robot = robot;
        setTitle("Maze");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MazePanel mazePanel = new MazePanel(maze, robot, this);
        add(mazePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // Eseguire il movimento
                    robot.move(maze);
                    moveCount++;
                    mazePanel.incrementMoveCount();
                    mazePanel.repaint();
                    if (robot.getPosition().equals(maze.getExitPosition())) {
                        dispose();
                        showFinalScreen();
                    }
                }
            }
        });
    }

    private void showFinalScreen() {
        FinalScreen finalScreen = new FinalScreen();
        finalScreen.setVisible(true);
    }
}

class MazePanel extends JPanel {
    private Maze maze;
    private Robot robot;
    private int moveCount = 0;
    private MazeScreen parent;

    public MazePanel(Maze maze, Robot robot, MazeScreen parent) {
        this.maze = maze;
        this.robot = robot;
        this.parent = parent;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegnare la matrice del labirinto

        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("Mosse: " + moveCount, 10, 20);
    }

    public void incrementMoveCount() {
        moveCount++;
    }
}
```

#### Schermata finale

```java
public class FinalScreen extends JFrame {
    public Final

Screen() {
        setTitle("High Scores");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        JLabel exitLabel = new JLabel("Premere SPACE per uscire", SwingConstants.CENTER);
        exitLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(exitLabel, BorderLayout.SOUTH);

        add(panel);

        loadHighScores(scoreArea);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                    showHomeScreen();
                }
            }
        });
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
    }

    private void showHomeScreen() {
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.setVisible(true);
    }
}
```

Questa struttura permette di gestire in modo ordinato le varie fasi del progetto, dall'inizializzazione del gioco alla gestione delle strategie di movimento del robot e della costruzione del labirinto. Assicurati di gestire le eccezioni e di aggiungere i necessari commenti e documentazione Javadoc per facilitare la manutenzione del codice.


Capisco. Se desideri mantenere tutto nella stessa finestra, puoi utilizzare un `CardLayout` per gestire le diverse schermate. Con `CardLayout`, puoi cambiare il contenuto della finestra senza dover chiudere e riaprire la finestra.

Ecco come puoi fare:

1. Crea un `CardLayout` nel frame principale.
2. Aggiungi i pannelli per le diverse schermate (`HomeScreen`, `HighScoreScreen`, ecc.) al `CardLayout`.
3. Usa `CardLayout.show()` per passare da una schermata all'altra.

### `MainFrame.java`
```java
package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("TheLabirinto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create screens
        HomeScreen homeScreen = new HomeScreen(this);
        HighScoreScreen highScoreScreen = new HighScoreScreen(this);

        // Add screens to the main panel
        mainPanel.add(homeScreen, "HomeScreen");
        mainPanel.add(highScoreScreen, "HighScoreScreen");

        add(mainPanel);

        // Show home screen initially
        cardLayout.show(mainPanel, "HomeScreen");
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
```

### `HomeScreen.java`
```java
package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HomeScreen extends JPanel {
    public HomeScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        // Create a panel for the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("TheLabirinto");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Create a panel for the icon and the message
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("images/START.png"));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Premere SPACE per proseguire");
        messageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(iconLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some space between icon and text
        centerPanel.add(messageLabel);

        add(centerPanel, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    mainFrame.showScreen("HighScoreScreen");
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }
}
```

### `HighScoreScreen.java`
```java
package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HighScoreScreen extends JPanel {
    public HighScoreScreen(MainFrame mainFrame) {
        setLayout(new BorderLayout());

        JTextArea scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Serif", Font.PLAIN, 20));
        add(new JScrollPane(scoreArea), BorderLayout.CENTER);

        JLabel exitLabel = new JLabel("Premere SPACE per uscire", SwingConstants.CENTER);
        exitLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        add(exitLabel, BorderLayout.SOUTH);

        loadHighScores(scoreArea);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    mainFrame.showScreen("HomeScreen");
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    private void loadHighScores(JTextArea scoreArea) {
        // Implementazione per caricare i punteggi dal database
        scoreArea.setText("Punteggio 1: 100\nPunteggio 2: 90\nPunteggio 3: 80\nPunteggio 4: 70\nPunteggio 5: 60");
    }
}
```

### Spiegazione:

1. **MainFrame**:
    - Crea un frame principale con un `CardLayout` che contiene i vari pannelli (schermate).
    - `showScreen(String screenName)` è un metodo per passare da una schermata all'altra.

2. **HomeScreen**:
    - Estende `JPanel` e implementa la schermata iniziale.
    - Ha un costruttore che accetta un riferimento a `MainFrame` per poter cambiare schermata.
    - Al rilevamento della pressione della barra spaziatrice, cambia alla schermata dei punteggi.

3. **HighScoreScreen**:
    - Estende `JPanel` e implementa la schermata dei punteggi.
    - Ha un costruttore che accetta un riferimento a `MainFrame` per poter cambiare schermata.
    - Al rilevamento della pressione della barra spaziatrice, cambia alla schermata iniziale.

Usando `CardLayout`, è possibile gestire diverse schermate all'interno della stessa finestra senza dover chiudere e riaprire la finestra.