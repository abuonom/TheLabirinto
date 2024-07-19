package com.thelabirinto.strategy;

import com.thelabirinto.builder.Position;
import com.thelabirinto.builder.Maze;

public class PlayerMovementStrategy implements MovementStrategy {
    private int dx;
    private int dy;

    public PlayerMovementStrategy(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Position getNextPosition(Maze maze, Position currentPos) {
        int newX = currentPos.getX() + dx;
        int newY = currentPos.getY() + dy;
        return new Position(newX, newY);
    }
}
