package tests;

import core.Board;
import gui.Game;
import org.junit.jupiter.api.Test;
import pieces.Piece;
import pieces.Rook;

import static org.junit.jupiter.api.Assertions.*;

public class RookTests {

    @Test
    public void legalMove() {
        Board b = new Board();
        Piece tower = new Rook(0, 7, b);
        Piece t = new Rook(5, 7, b);
        b.getPieces().add(tower);
        b.getPieces().add(t);

        assertFalse(tower.isLegalMove(6 * Game.TILES_SIZE, 7 * Game.TILES_SIZE));
    }

}
