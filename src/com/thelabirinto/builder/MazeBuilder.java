package com.thelabirinto.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class MazeBuilder {
    private final Random random = new Random();
    private final int windowWidth;
    private final int windowHeight;
    private int[][]map;
    private Position robotPosition;
    private Position exitPosition;
    
    public MazeBuilder(int windowWidth, int windowHeight, int tileSize) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        int rows = windowHeight / tileSize;
        int cols = windowWidth / tileSize;
        this.map = new int[rows][cols];
    }
    
    public MazeBuilder addExit() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 0:
                exitPosition = new Position(0,random.nextInt(map[0].length));
                break;
            case 1:
                exitPosition = new Position(random.nextInt(map.length - 1), 0);
                break;
            case 2:
                exitPosition = new Position(random.nextInt(map.length - 1), random.nextInt(map[0].length));
                break;
            case 3:
                exitPosition = new Position(map.length - 1, random.nextInt(map[0].length));
        }
        map[exitPosition.getX()][exitPosition.getY()] = 2;
        return this;
    }

    public MazeBuilder addRobot() {
        List<Position> freePositions = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 0) {
                    freePositions.add(new Position(row, col));
                }
            }
        }
        if (!freePositions.isEmpty()) {
            robotPosition = freePositions.get(random.nextInt(freePositions.size()));
            map[robotPosition.getX()][robotPosition.getY()] = 3;
        }
        return this;
    }

    public MazeBuilder addWalls() {
        int maxObstacles = random.nextInt((map.length * map[0].length) / 2); //50% del totale delle celle
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

    public Maze build(){
        return new Maze(map,robotPosition,exitPosition,windowWidth,windowHeight);
    }
}
