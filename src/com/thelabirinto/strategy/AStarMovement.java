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
        Map<Position, Double> gScore = new HashMap<>();
        Map<Position, Double> fScore = new HashMap<>();
        PriorityQueue<Position> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));
        Map<Position, Position> cameFrom = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Position current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current).get(1); // return the next position in the path
            }

            closedSet.add(current);

            for (Position neighbor : getNeighbors(maze, current)) {
                if (closedSet.contains(neighbor)) {
                    continue; // skip this neighbor, it's already evaluated
                }

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + distanceBetween(current, neighbor);

                if (!openSet.contains(neighbor) || tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // No path found, return start position
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

    private double heuristic(Position a, Position b) {
        // Manhattan distance heuristic
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private double distanceBetween(Position a, Position b) {
        // Assuming a simple grid distance (1 for cardinal directions, sqrt(2) for diagonals)
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<Position> getNeighbors(Maze maze, Position pos) {
        List<Position> neighbors = new ArrayList<>();
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, // cardinal directions
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // diagonal directions
        };

        for (int[] direction : directions) {
            int newX = pos.getX() + direction[0];
            int newY = pos.getY() + direction[1];
            Position neighbor = new Position(newX, newY);
            if (maze.isValidMove(newX, newY)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
