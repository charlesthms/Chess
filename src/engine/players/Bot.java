package engine.players;

import engine.Board;
import engine.moves.Move;
import pieces.King;

import java.util.ArrayList;

public class Bot extends Player {
    public Bot(Board board, King king, boolean isWhite) {
        super(board, king, isWhite);
    }

    public void play() {
        ArrayList<Move> moves = board.generateMoves();

        if (moves.size() == 0) {
            System.out.println("CHECKMATE");
        } else {
            int index = (int) (Math.random() * (moves.size()));
            System.out.println(moves.size());
            System.out.println(index);
            board.doMove(moves.get(index));
        }

    }
}
