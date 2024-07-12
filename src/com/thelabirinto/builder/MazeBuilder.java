package com.thelabirinto.builder;


import java.util.Random;

public final class MazeBuilder {
    private final Random random = new Random();
    private final int windowWidth;
    private final int windowHeight;
    private final int tileSize;
    private final int[][] map;
    private Position robotPosition;
    private Position exitPosition;
    private final double difficult;
    private Player player;

    public MazeBuilder(int windowWidth, int windowHeight, int tileSize, double difficult) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.tileSize = tileSize;
        this.difficult = difficult;
        int rows = windowHeight / tileSize;
        int cols = windowWidth / tileSize;
        this.map = new int[rows][cols];
    }

    public MazeBuilder addExit() {
        exitPosition = new Position(0, random.nextInt(map[0].length));
        map[exitPosition.getX()][exitPosition.getY()] = 2;
        return this;
    }

    public MazeBuilder addRobot() {
        int lastRow = map.length - 1;
        int minCol = 1;
        int maxCol = map[lastRow].length - 2;
        int col = minCol + random.nextInt(maxCol - minCol + 1);

        robotPosition = new Position(lastRow, col);
        map[robotPosition.getX()][robotPosition.getY()] = 3;

        return this;
    }

    public MazeBuilder addWalls() {
        int maxObstacles = (int) ((map.length * map[0].length) / difficult);
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

    public MazeBuilder setPlayer(String name, String surname) {
        this.player = new Player(name,surname,0);
        return this;
    }

    public Maze build() {
        return new Maze(map, robotPosition, exitPosition, windowWidth, windowHeight, tileSize, player);
    }
}
