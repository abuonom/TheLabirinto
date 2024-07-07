package com.thelabirinto.strategy;

import com.thelabirinto.builder.Position;
import com.thelabirinto.builder.Maze;

import java.util.*;

public class AStarMovement implements MovementStrategy {

    @Override
    public Position getNextPosition(Maze maze, Position currentPos) {
        Position goal = maze.getExitPosition();
        return aStar(maze, currentPos, goal);
    }

    private Position aStar(Maze maze, Position start, Position goal) {
        Set<Position> closedSet = new HashSet<>();
        Set<Position> openSet = new HashSet<>();
        openSet.add(start);

        Map<Position, Position> cameFrom = new HashMap<>();
        Map<Position, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        Map<Position, Integer> fScore = new HashMap<>();
        fScore.put(start, heuristic(start, goal));

        while (!openSet.isEmpty()) {
            Position current = getLowestFScore(openSet, fScore);

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current).get(1);
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Position neighbor : getNeighbors(maze, current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristic(neighbor, goal));
            }
        }

        return start;
    }

    private List<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        List<Position> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    private Position getLowestFScore(Set<Position> openSet, Map<Position, Integer> fScore) {
        Position lowest = null;
        int minScore = Integer.MAX_VALUE;
        for (Position pos : openSet) {
            int score = fScore.getOrDefault(pos, Integer.MAX_VALUE);
            if (score < minScore) {
                minScore = score;
                lowest = pos;
            }
        }
        return lowest;
    }

    private int heuristic(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private List<Position> getNeighbors(Maze maze, Position pos) {
        List<Position> neighbors = new ArrayList<>();
        int x = pos.getX();
        int y = pos.getY();

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, // cardinal directions
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // diagonal directions
        };

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (maze.isValidMove(newX, newY)) {
                neighbors.add(new Position(newX, newY));
            }
        }
        return neighbors;
    }
}
