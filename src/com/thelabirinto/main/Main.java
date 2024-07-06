package com.thelabirinto.main;

import com.thelabirinto.builder.Maze;
import com.thelabirinto.builder.MazeBuilder;
import com.thelabirinto.graphics.MainFrame;

public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        int windowWidth = 600;
        int windowHeight = 600;
        int tileSize = 40;

        MazeBuilder builder;
        Maze maze;

        do {
            builder = new MazeBuilder(windowWidth, windowHeight, tileSize);
            maze = builder.addEdges()
                    .addWalls()
                    .addExit()
                    .addRobot()
                    .build();
        } while (!maze.isPlayable());
            System.out.println(maze);
    }
    }
