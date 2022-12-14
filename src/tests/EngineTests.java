package tests;

import engine.Board;
import engine.moves.Move;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class EngineTests {

    @Test
    public void classicStartTest() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        //assertEquals(400, perft(2, 2, board));
        //assertEquals(8902, perft(3, 3, board));
        assertEquals(197281, perft(4, 4, board));
    }

    @Test
    public void kiwipeteTest() {
        Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");

        //assertEquals(48, perft(1, board));
        assertEquals(2039, perft(2, 2, board));
        //assertEquals(97862, perft(3, board));
    }


    public long perft(int depth, int maxDepth, Board board)
    {
        if (depth == 0) {
            return 1;
        }

        int nodes = 0;
        ArrayList<Move> moves = board.generateMoves();
        for (Move move : moves)
        {
            board.doMove(move);

            long n = perft(depth - 1, maxDepth, board);
            nodes += n;
            if (depth == maxDepth) System.out.println("move: " + move + "      nodes: " + n);

            board.undoMove(move);
        }

        return nodes;
    }
}
