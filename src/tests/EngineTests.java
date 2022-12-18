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

        //assertEquals(20, perft(1, 1, board));
        //assertEquals(400, perft(2, 2, board));
        //assertEquals(8902, perft(3, 3, board));
        //assertEquals(197281, perft(4, 4, board));
        assertEquals(4865609, perft(5, 5, board));
    }

    @Test
    public void kiwipeteTest() {
        Board board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");

        //assertEquals(48, perft(1, 1, board));
        //assertEquals(2039, perft(2, 2, board));
        assertEquals(97862, perft(3, 3, board));
    }

    @Test
    public void thirdTest() {
        Board board = new Board("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ");

        //assertEquals(191, perft(2, 2, board));
        assertEquals(2812, perft(3, 3, board));
    }

    @Test
    public void test() {
        Board board = new Board("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");

        assertEquals(3894594, perft(4, 4, board));
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
