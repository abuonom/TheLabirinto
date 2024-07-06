package com.thelabirinto.builder;

public class Maze {
    private int[][] map;
    private Position robotPosition;
    private final Position exitPosition;
    private final int windowWidth;
    private final int windowHeight;
    private final int tileSize;

    public int getTileSize() {
        return tileSize;
    }

    public Maze(int[][] map, Position robotPosition, Position exitPosition, int windowWidth, int windowHeight, int tileSize) {
        this.map = map;
        this.robotPosition = robotPosition;
        this.exitPosition = exitPosition;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.tileSize = tileSize;
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

    public boolean isPlayable() {
        if (robotPosition == null || exitPosition == null) {
            return false;
        }

        boolean[][] visited = new boolean[map.length][map[0].length];

        return floodFill(robotPosition.getX(), robotPosition.getY(), visited);
    }


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
        System.out.println("EXIT POSITION" + this.exitPosition);
        System.out.println("ROBOT POSITION" + this.robotPosition);
        StringBuilder sb = new StringBuilder();
        for (int[] row : map) {
            for (int cell : row) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void updateRobot(Position newRobotPosition) {
        map[robotPosition.getX()][robotPosition.getY()] = 0;
        robotPosition = newRobotPosition;
        map[robotPosition.getX()][robotPosition.getY()] = 3;
    }
}
