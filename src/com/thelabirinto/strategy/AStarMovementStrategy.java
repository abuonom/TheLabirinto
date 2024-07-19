package com.thelabirinto.strategy;

import com.thelabirinto.builder.Position;
import com.thelabirinto.builder.Maze;

import java.util.*;

/**
 * Implementazione della strategia di movimento A* per trovare il percorso ottimale nel labirinto.
 */
public class AStarMovementStrategy implements MovementStrategy {

    /**
     * Restituisce la prossima posizione nel labirinto usando l'algoritmo A*.
     *
     * @param maze il labirinto in cui si sta muovendo
     * @param currentPos la posizione attuale
     * @return la prossima posizione
     */
    @Override
    public Position getNextPosition(Maze maze, Position currentPos) {
        Position goal = maze.getExitPosition();

        Set<Position> closedSet = new HashSet<>();
        Set<Position> openSet = new HashSet<>();
        openSet.add(currentPos);

        Map<Position, Position> cameFrom = new HashMap<>();

        Map<Position, Integer> gScore = new HashMap<>();
        gScore.put(currentPos, 0);

        Map<Position, Integer> fScore = new HashMap<>();
        fScore.put(currentPos, heuristicCostEstimate(currentPos, goal));

        while (!openSet.isEmpty()) {
            Position current = getLowestFScore(openSet, fScore);
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current).get(1); // Ottieni la prossima posizione nel percorso
            }

            openSet.remove(current);
            closedSet.add(current);

            for (Position neighbor : getNeighbors(current, maze)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = gScore.get(current) + 1; // Assumendo che il costo tra i nodi sia sempre 1

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal));
            }
        }

        return null; // Nessun percorso trovato
    }

    /**
     * Stima il costo euristico tra due posizioni.
     *
     * @param a la posizione iniziale
     * @param b la posizione finale
     * @return il costo euristico
     */
    private int heuristicCostEstimate(Position a, Position b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * Ottiene la posizione con il punteggio f più basso dall'insieme aperto.
     *
     * @param openSet l'insieme aperto
     * @param fScore la mappa dei punteggi f
     * @return la posizione con il punteggio f più basso
     */
    private Position getLowestFScore(Set<Position> openSet, Map<Position, Integer> fScore) {
        Position lowest = null;
        int lowestScore = Integer.MAX_VALUE;

        for (Position pos : openSet) {
            int score = fScore.getOrDefault(pos, Integer.MAX_VALUE);
            if (score < lowestScore) {
                lowestScore = score;
                lowest = pos;
            }
        }

        return lowest;
    }

    /**
     * Ottiene i vicini validi di una posizione nel labirinto.
     *
     * @param pos la posizione attuale
     * @param maze il labirinto
     * @return la lista delle posizioni vicine
     */
    private List<Position> getNeighbors(Position pos, Maze maze) {
        List<Position> neighbors = new ArrayList<>();
        int x = pos.getX();
        int y = pos.getY();

        if (maze.isValidMove(x + 1, y)) {
            neighbors.add(new Position(x + 1, y));
        }
        if (maze.isValidMove(x - 1, y)) {
            neighbors.add(new Position(x - 1, y));
        }
        if (maze.isValidMove(x, y + 1)) {
            neighbors.add(new Position(x, y + 1));
        }
        if (maze.isValidMove(x, y - 1)) {
            neighbors.add(new Position(x, y - 1));
        }

        return neighbors;
    }

    /**
     * Ricostruisce il percorso dal punto di partenza alla destinazione.
     *
     * @param cameFrom la mappa delle posizioni precedenti
     * @param current la posizione attuale
     * @return la lista delle posizioni nel percorso
     */
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
}
