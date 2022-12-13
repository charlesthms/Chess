package engine.moves;

import gui.Game;
import pieces.Piece;

public class CastlingMove extends Move {

    private Piece rook;

    /**
     * Créer un mouvement
     *
     * @param piece    Pièce source
     * @param tx       Coordonnée x en cases de la cible
     * @param ty       Coordonnée y en cases de la cible
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public CastlingMove(Piece piece, int tx, int ty, boolean isLethal, Piece rook) {
        super(piece, tx, ty, isLethal);
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
