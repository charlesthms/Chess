package inputs;

import gui.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    private Game game;

    public Mouse(Game game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        game.getBoardManager().mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        game.getBoardManager().mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        game.getBoardManager().mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        game.getBoardManager().mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        game.getBoardManager().mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        game.getBoardManager().mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        game.getBoardManager().mouseMoved(e);
    }
}
