package core;

import gui.Game;
import pieces.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PromoteMove extends Move {

    private Game game;

    /**
     * Créer un mouvement
     *
     * @param x        Coordonnée x en cases
     * @param y        Coordonnée y en cases
     * @param isLethal true si le coup engendre la mort d'une autre pièce
     */
    public PromoteMove(int x, int y, boolean isLethal, Game game) {
        super(x, y, isLethal);
        this.game = game;
    }

    public Piece showPromoteDialog(Piece p) {
        JLabel funLabel = new JLabel("Vous avez atteint le sommet de l'échiquier !\n");
        JLabel promoteLabel = new JLabel("Promouvoir le pion en: ");
        setBoldAndBorder(promoteLabel);
        JRadioButton queenRadioButton = new JRadioButton("♕ Reine", true);
        JRadioButton rookRadioButton = new JRadioButton("♖ Tour", false);
        JRadioButton bishopRadioButton = new JRadioButton("♗ Fou", false);
        JRadioButton knightRadioButton = new JRadioButton("♘ Cavalier", false);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(queenRadioButton);
        buttonGroup.add(rookRadioButton);
        buttonGroup.add(bishopRadioButton);
        buttonGroup.add(knightRadioButton);

        final JComponent[] inputs = new JComponent[] {
                funLabel,
                new JSeparator(),
                promoteLabel,
                queenRadioButton,
                rookRadioButton,
                bishopRadioButton,
                knightRadioButton
        };

        JOptionPane.showConfirmDialog(game.getGamePanel(), inputs, "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE);

        Piece piece;
        if (rookRadioButton.isSelected()) {
            piece = new Rook(p.getXp(), p.getYp(), game.getBoardManager(), p.isWhite());
        } else if (bishopRadioButton.isSelected()) {
            piece = new Bishop(p.getXp(), p.getYp(), game.getBoardManager(), p.isWhite());
        } else if (knightRadioButton.isSelected()) {
            piece = new Knight(p.getXp(), p.getYp(), game.getBoardManager(), p.isWhite());
        } else {
            piece = new Queen(p.getXp(), p.getYp(), game.getBoardManager(), p.isWhite());
        }

        return piece;
    }

    private void setBoldAndBorder(JLabel label) {
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
    }
}
