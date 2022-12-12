package core;

import gui.Game;
import pieces.Piece;

public class CastlingMove extends Move {

    private Piece rook;

    /**
     * Créer un mouvement de castle
     *
     * @param x        Coordonnée x en cases
     * @param y        Coordonnée y en cases
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     * @param rook     La tour du roque
     */
    public CastlingMove(int x, int y, boolean isLethal, Piece rook) {
        super(x, y, isLethal);
        this.rook = rook;
    }

    public void moveRook() {
        if (rook.getXp() > 4) {
            rook.updatePosition((rook.getXp() - 2) * Game.TILES_SIZE, rook.getY());
        } else {
            rook.updatePosition((rook.getXp() + 3) * Game.TILES_SIZE, rook.getY());
        }
    }
}
