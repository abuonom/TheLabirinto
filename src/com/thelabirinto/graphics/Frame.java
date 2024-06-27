package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame implements KeyListener {
    Panel panel;
    JLabel label;
    ImageIcon robot;

    public Frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640,640);
        this.setTitle("The Labirinto");
        this.setLayout(null);
        this.addKeyListener(this);
        robot = new ImageIcon("images/START.png");
        panel = new Panel();
        panel.setBounds(0, 0, 640, 640);
        this.add(panel);

        label = new JLabel();
        label.setBounds(50,50,64,64);
        label.setIcon(robot);
        this.add(label);
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //invoced when a key is typed. Uses KeyChar, char output
        if(keyEvent.getKeyChar() == KeyEvent.VK_SPACE)
            System.out.println("BARRA SPAZIATRICE PREMUTA");
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        //Invoked when a physical key is pressed down. Uses KeyCode, int outpud
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //Called when a button is released
    }
}
