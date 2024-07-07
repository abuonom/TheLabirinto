package com.thelabirinto.strategy;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMovement implements MovementStrategy{
    @Override
    public Position getNextPosition(Maze maze, Position currentPosition) {
        List<Position> possibleMoves = new ArrayList<>();
        int x = currentPosition.getX();
        int y = currentPosition.getY();

        if (maze.isValidMove(x + 1, y))
            possibleMoves.add(new Position(x, y));
        if (maze.isValidMove(x - 1, y))
            possibleMoves.add(new Position(x, y));
        if (maze.isValidMove(x, y + 1))
            possibleMoves.add(new Position(x, y + 1));
        if (maze.isValidMove(x, y - 1))
            possibleMoves.add(new Position(x, y - 1));

        if (possibleMoves.isEmpty())
            return currentPosition;

        Random random = new Random();
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

}
