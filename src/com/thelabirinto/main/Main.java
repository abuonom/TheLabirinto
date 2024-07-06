package com.thelabirinto.main;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;
import com.thelabirinto.graphics.MainFrame;

public class Main {
    public static void main(String[] args) {

        MazeBuilder builder;
        Maze maze;

        do {
            builder = new MazeBuilder(600, 600, 64);
            maze = builder.addEdges()
                    .addWalls()
                    .addExit()
                    .addRobot()
                    .build();
        } while (!maze.isPlayable());
            System.out.println(maze);
        MainFrame mainFrame = new MainFrame(maze);
        mainFrame.setVisible(true);
    }
    }
