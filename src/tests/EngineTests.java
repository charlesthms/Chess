package tests;

import engine.Board;
import engine.moves.Move;
import org.junit.jupiter.api.Test;
import pieces.Piece;

import java.util.ArrayList;

public class EngineTests {

    @Test
    public void moveGenerationTest() {
        Board board = new Board();

        System.out.println(perft(1, board));
    }

    public long perft(int depth, Board board) {
        if (depth == 0) {
            return 1;
        }

        int nodes = 0;
        ArrayList<Move> moves = board.generateMoves();
        for (Move move : moves) {
            board.doMove(move);
            nodes += perft(depth - 1, board);
            board.undoMove(move);
        }
        return nodes;
    }
}
