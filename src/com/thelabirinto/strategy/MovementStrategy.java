package com.thelabirinto.strategy;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.Position;

public interface MovementStrategy {
    Position getNextPosition(Maze mazen, Position currentPosition);
}
