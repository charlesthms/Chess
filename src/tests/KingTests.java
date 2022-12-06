package tests;

import core.Board;
import core.Move;
import gui.Game;
import org.junit.jupiter.api.Test;
import pieces.Bishop;
import pieces.King;
import pieces.Piece;
import pieces.Rook;

import static org.junit.jupiter.api.Assertions.*;

public class KingTests {

    @Test
    public void checkTest() {
        Board b = new Board();

        Piece bishop = new Bishop(7, 0, b);
        bishop.updatePosition(7 * Game.TILES_SIZE, 2 * Game.TILES_SIZE);
        King king = new King(4, 7, b);

        for (Move m : bishop.getLegalMoves()) {
            System.out.printf("[%d, %d, %s]\n", m.getX() / Game.TILES_SIZE, m.getY() / Game.TILES_SIZE, m.isLethal());
        }


    }
}
