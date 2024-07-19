package com.thelabirinto.builder;

import com.thelabirinto.graphics.Difficulty;

import java.util.Random;

/**
 * Classe che rappresenta il labirinto che viene visualizzato a schermo, presenta anche i dati
 * del Player al suo interno.
 */
public class Maze {
    private final int[][] map;
    private Position robotPosition;
    private final Position exitPosition;
    private final int windowWidth;
    private final int windowHeight;
    private final int tileSize;
    private final Player player;

    /**
     * Costruttore della classe Maze
     * @param map
     * @param robotPosition
     * @param exitPosition
     * @param windowWidth
     * @param windowHeight
     * @param tileSize
     * @param player
     */
    public Maze(int[][] map, Position robotPosition, Position exitPosition, int windowWidth, int windowHeight, int tileSize, Player player) {
        this.map = map;
        this.robotPosition = robotPosition;
        this.exitPosition = exitPosition;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.tileSize = tileSize;
        this.player = player;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int[][] getMap() {
        return map;
    }

    public Position getRobotPosition() {
        return robotPosition;
    }

    public Position getExitPosition() {
        return exitPosition;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * Consente tramite l'utilizzo dell'algoritmo ricorsivo floodfill
     * la verifica della giocabilità della mappa (robot riesce a
     * raggiungere dalla sua posizione l'uscita)
     * @return false se la mappa è giocabile
     */
    public boolean isPlayable() {
        if (robotPosition == null || exitPosition == null) {
            return true;
        }

        boolean[][] visited = new boolean[map.length][map[0].length];

        return !floodFill(robotPosition.getX(), robotPosition.getY(), visited);
    }

    /**
     * Verifica ricorsivamente le 4 caselle vicine alla posizione selezionata
     * e le imposta a true se le ha già visitate, quando a partire dal robot
     * riesce ad arrivare all'uscita termina
     * @param x coordinata della mappa
     * @param y coordinata della mappa
     * @param visited coordinata della mappa
     * @return true se la mappa è valida, false se la mappa non è valida
     */
    private boolean floodFill(int x, int y, boolean[][] visited) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length || visited[x][y] || map[x][y] == 1) {
            return false;
        }

        if (map[x][y] == 2) {
            return true;
        }

        visited[x][y] = true;

        return floodFill(x + 1, y, visited) ||
                floodFill(x - 1, y, visited) ||
                floodFill(x, y + 1, visited) ||
                floodFill(x, y - 1, visited);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : map) {
            for (int cell : row) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Aggiorna la posizione del robot all'interno della mappa
     * @param newRobotPosition
     */
    public void updateRobot(Position newRobotPosition) {
        map[robotPosition.getX()][robotPosition.getY()] = 0;
        robotPosition = newRobotPosition;
        map[robotPosition.getX()][robotPosition.getY()] = 3;
    }


    /**
     *
     * @param x
     * @param y
     * @return false se la mossa non è valida, true se lo è
     */
    public boolean isValidMove(int x, int y) {
        // Verifica se le coordinate sono all'interno dei limiti della mappa
        if (x < 0 || x >= this.getMap().length || y < 0 || y >= this.getMap()[0].length) {
            return false;
        }
        // Verifica se la casella sulla mappa è attraversabile (valore 0)
        return (this.getMap()[x][y] == 0 || this.getMap()[x][y] == 2);
    }

    /**
     * Rigenera la mappa ogni qualvolta viene invocato.
     * Attraverso il floodfill controlla che la mappa sia
     * giocabile prima di metterla a schermo.
     * @param difficulty
     */
    public void regenerateMap(Difficulty difficulty) {
        Random random = new Random();
        int maxObstacles = (int) ((map.length * map[0].length) / difficulty.getValue()); //numero di ostacoli massimo
        int obstacle;
        int x, y;

        do {
            obstacle = 0;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (i == 0 || i == map.length - 1 || j == 0 || j == map[0].length - 1) {
                        map[i][j] = 1;
                    } else if (map[i][j] != 3 && map[i][j] != 2) {
                        map[i][j] = 0;
                    }
                }
            }
            while (obstacle < maxObstacles) {
                x = random.nextInt(map.length - 2) + 1;
                y = random.nextInt(map[0].length - 2) + 1;
                if (map[x][y] == 0) {
                    map[x][y] = 1;
                    obstacle++;
                }
            }
            map[robotPosition.getX()][robotPosition.getY()] = 3;
            map[exitPosition.getX()][exitPosition.getY()] = 2;
        } while (isPlayable()); //finchè isPlayable non restituisce false, non esce
    }

    public Player getPlayer() {
        return player;
    }
}
