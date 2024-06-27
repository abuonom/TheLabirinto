package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Panel extends JPanel{
    final Integer SCREEN_WIDTH = 640; //640
    final Integer SCREEN_HEIGHT = 640; //640
    int x = 0;
    int y = 0;
    int xVelocity = 1;
    int yVelocity = 1;
    Image floorTile;
    Image wallTile;
    Image robotTile;
    Timer timer;
    Graphics2D g2d;

    public Panel() {
        floorTile = new ImageIcon("images/BACK.png").getImage();
        wallTile = new ImageIcon("images/WALL.png").getImage();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g2d = (Graphics2D) g;
        g2d.drawImage(wallTile,0,0,32,32,null);
        g2d.drawImage(floorTile,30,0,32,32,null);
    }
}
