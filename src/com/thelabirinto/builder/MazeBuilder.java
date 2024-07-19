package com.thelabirinto.builder;
import com.thelabirinto.graphics.Difficulty;

import java.util.Random;

/**
 * Classe costruita tramite Pattern Builder, serve a costruire l'oggetto Maze
 */
public final class MazeBuilder extends Player {
    private final Random random = new Random();
    private final int windowWidth;
    private final int windowHeight;
    private final int tileSize;
    private final int[][] map;
    private Position robotPosition;
    private Position exitPosition;
    private final Difficulty difficulty;
    private Player player;

    /**
     * Costruttore del builder
     * @param windowWidth larghezza della finestra
     * @param windowHeight altezza della finestra
     * @param tileSize grandezza delle tile
     * @param difficulty difficoltà selezionata dal giocatore
     */
    public MazeBuilder(int windowWidth, int windowHeight, int tileSize, Difficulty difficulty) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.tileSize = tileSize;
        this.difficulty = difficulty;
        int rows = windowHeight / tileSize;
        int cols = windowWidth / tileSize;
        this.map = new int[rows][cols];
    }

    /**
     * Genera il parametro exitPosition di Maze
     * @return istanza del builder
     */
    public MazeBuilder addExit() {
        exitPosition = new Position(0, random.nextInt(map[0].length));
        map[exitPosition.getX()][exitPosition.getY()] = 2;
        return this;
    }
    /**
     * Genera il parametro robotPosition sempre su una riga casuale dell'ultima colonna
     * @return istanza del builder
     */
    public MazeBuilder addRobot() {
        int lastRow = map.length - 1;
        int minCol = 1;
        int maxCol = map[lastRow].length - 2;
        int col = minCol + random.nextInt(maxCol - minCol + 1);

        robotPosition = new Position(lastRow, col);
        map[robotPosition.getX()][robotPosition.getY()] = 3;

        return this;
    }

    /**
     * Genera gli ostacoli nella mappa, calcolando il loro numero in base alla grandezza della mappa
     * e alla difficoltà selezionata dal giocore
     * @return istanza builder
     */
    public MazeBuilder addWalls() {
        int maxObstacles = (int) ((map.length * map[0].length) / difficulty.getValue());
        int obstacle = 0;
        int x;
        int y;
        while (obstacle < maxObstacles) {
            x = random.nextInt(map.length - 1);
            y = random.nextInt(map[0].length - 1);
            if (map[x][y] == 0) {
                map[x][y] = 1;
                obstacle++;
            }
        }
        return this;
    }

    /**
     * Aggiunge i bordi alla mappa
     * @return istanza builder
     */
    public MazeBuilder addEdges() {
        for (int i = 0; i < map.length; i++) {
            map[i][0] = 1;
            map[i][map[0].length - 1] = 1;
        }
        for (int j = 0; j < map[0].length; j++) {
            map[0][j] = 1;
            map[map.length - 1][j] = 1;
        }
        return this;
    }

    /**
     * Genera il parametro Player di Maze
     * @param name
     * @param surname
     * @return
     */
    public MazeBuilder setPlayer(String name, String surname) {
        this.player = new Player(name,surname,0,difficulty.getName());
        return this;
    }

    /**
     * Costruisce l'oggetto Maze
     * @return
     */
    public Maze build() {
        return new Maze(map, robotPosition, exitPosition, windowWidth, windowHeight, tileSize, player);
    }
}
