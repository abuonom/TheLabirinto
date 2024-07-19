package com.thelabirinto.strategy;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.Position;

/**
 * Interfaccia che rappresenta una strategia di movimento nel labirinto, sfrutta le proprietò del
 * pattern Strategy
 */
public interface MovementStrategy {
    /**
     * Restituisce la prossima posizione nel labirinto in base alla strategia di movimento.
     *
     * @param maze il labirinto in cui si sta muovendo
     * @param currentPosition la posizione attuale
     * @return la prossima posizione
     */
    Position getNextPosition(Maze maze, Position currentPosition);
}
