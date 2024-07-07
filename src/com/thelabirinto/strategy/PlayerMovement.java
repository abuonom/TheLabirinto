package com.thelabirinto.strategy;

import com.thelabirinto.builder.Position;
import com.thelabirinto.builder.Maze;
import com.thelabirinto.strategy.MovementStrategy;

public class PlayerMovement implements MovementStrategy {
    private int dx;
    private int dy;

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Position getNextPosition(Maze maze, Position currentPos) {
        int newX = currentPos.getX() + dx;
        int newY = currentPos.getY() + dy;
        if (maze.isValidMove(newX,newY))
            return new Position(newX, newY);
        return currentPos;
    }
}
