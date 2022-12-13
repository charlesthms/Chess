package gui;

import inputs.Keyboard;
import inputs.Mouse;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        addMouseListener(new Mouse(game));
        addMouseMotionListener(new Mouse(game));
        addKeyListener(new Keyboard(game));
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game.WIDTH, Game.HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public void update() {

    }
}
