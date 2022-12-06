package gui;

import javax.swing.*;

public class GameWindow {

    private final JFrame frame;

    public GameWindow(GamePanel gamePanel) {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}