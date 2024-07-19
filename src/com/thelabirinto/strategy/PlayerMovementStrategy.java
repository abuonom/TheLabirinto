package com.thelabirinto.strategy;

import com.thelabirinto.builder.Position;
import com.thelabirinto.builder.Maze;

/**
 * Strategia di movimento del giocatore nel labirinto.
 */
public class PlayerMovementStrategy implements MovementStrategy {
    private int dx;
    private int dy;

    /**
     * Costruttore che imposta la direzione iniziale del movimento.
     *
     * @param dx la variazione nella coordinata x
     * @param dy la variazione nella coordinata y
     */
    public PlayerMovementStrategy(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Imposta la direzione del movimento.
     *
     * @param dx la nuova variazione nella coordinata x
     * @param dy la nuova variazione nella coordinata y
     */
    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Restituisce la prossima posizione nel labirinto in base alla direzione impostata.
     *
     * @param maze il labirinto in cui si sta muovendo
     * @param currentPos la posizione attuale
     * @return la prossima posizione
     */
    @Override
    public Position getNextPosition(Maze maze, Position currentPos) {
        int newX = currentPos.getX() + dx;
        int newY = currentPos.getY() + dy;
        return new Position(newX, newY);
    }
}
