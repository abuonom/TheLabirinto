package com.thelabirinto.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class HomeScreen extends JPanel {
    public HomeScreen(MainFrame mainFrame){
        setLayout(new BorderLayout());

        //Create a panel for the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("TheLabirinto");
        titleLabel.setFont(new Font("Serif",Font.BOLD,40));
        titlePanel.add(titleLabel);
        add(titlePanel,BorderLayout.NORTH);

        //Create a panel for icon and message
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("images/START.png"));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Premere SPACE per proseguire");
        messageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(iconLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add some space between icon and text
        centerPanel.add(messageLabel);

        add(centerPanel, BorderLayout.CENTER);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    mainFrame.showScreen("HighScoreScreen");
                    mainFrame.getHighScoreScreen().requestFocusInWindow();
                }
            }
        });
    }
}
