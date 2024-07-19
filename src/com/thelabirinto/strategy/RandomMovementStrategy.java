package com.thelabirinto.strategy;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementazione della strategia di movimento casuale nel labirinto.
 */
public class RandomMovementStrategy implements MovementStrategy{

    /**
     * Restituisce la prossima posizione nel labirinto scegliendo casualmente tra le mosse valide.
     *
     * @param maze il labirinto in cui si sta muovendo
     * @param currentPosition la posizione attuale
     * @return la prossima posizione
     */
    @Override
    public Position getNextPosition(Maze maze, Position currentPosition) {
        List<Position> possibleMoves = new ArrayList<>();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        if (maze.isValidMove(x + 1, y)) {
            possibleMoves.add(new Position(x + 1, y)); // aggiorna la coordinata correttamente
        }
        if (maze.isValidMove(x - 1, y)) {
            possibleMoves.add(new Position(x - 1, y)); // aggiorna la coordinata correttamente
        }
        if (maze.isValidMove(x, y + 1)) {
            possibleMoves.add(new Position(x, y + 1)); // aggiorna la coordinata correttamente
        }
        if (maze.isValidMove(x, y - 1)) {
            possibleMoves.add(new Position(x, y - 1)); // aggiorna la coordinata correttamente
        }
        if (possibleMoves.isEmpty()) {
            return currentPosition;
        }

        Random random = new Random();
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }
}
