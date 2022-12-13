package gui;

import engine.Board;

import javax.swing.*;

public class GameWindow {

    private final JFrame frame;
    private Game game;

    private JMenuItem itemNew;
    private JMenuItem itemSave;
    private JMenuItem itemLoad;
    private JMenuItem itemPrintToConsole;
    private JMenuItem itemSuggestMove;
    private JMenuItem itemThreats;
    private JMenuItem itemAbout;

    public GameWindow(GamePanel gamePanel, Game game) {
        this.game = game;
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        setMenu();
        frame.pack();
        frame.setVisible(true);

    }

    private void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Chess");
        menuBar.add(fileMenu);

        JMenuItem itemExit = new JMenuItem("Quitter");
        fileMenu.add(itemExit);
        itemExit.addActionListener(actionEvent -> exit());

        JMenu gameMenu = new JMenu("Partie");
        menuBar.add(gameMenu);

        this.itemNew = new JMenuItem("Nouvelle");
        gameMenu.add(itemNew);
        gameMenu.addSeparator();
        this.itemSave = new JMenuItem("Sauvegarder");
        gameMenu.add(itemSave);
        this.itemLoad = new JMenuItem("Charger");
        gameMenu.add(itemLoad);
        gameMenu.addSeparator();

        this.itemSuggestMove = new JMenuItem("Afficher les coups possibles");
        gameMenu.add(itemSuggestMove);
        gameMenu.addSeparator();
        itemSuggestMove.addActionListener(actionEvent -> {
            Board.showTips = !Board.showTips;
        });

        this.itemThreats = new JMenuItem("Afficher les menaces");
        gameMenu.add(itemThreats);
        itemThreats.addActionListener(actionEvent -> {
            Board.showThreats = !Board.showThreats;
        });

        JMenu debugMenu = new JMenu("Debug");
        menuBar.add(debugMenu);

        this.itemPrintToConsole = new JMenuItem("Afficher le plateau");
        debugMenu.add(itemPrintToConsole);
        itemPrintToConsole.addActionListener(actionEvent -> {
            game.getBoardManager().printBoard();
        });

        JMenu helpMenu = new JMenu("Aide");
        menuBar.add(helpMenu);

        this.itemAbout = new JMenuItem("A propos");
        helpMenu.add(itemAbout);
        itemAbout.addActionListener(actionEvent -> showAboutDialog());
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame,
                "ChessEngine 1.0 (12.12.2022)\n"
                        + " \n"
                        + "Made with ♥ by Charles Thomas\n"
                        + " \n"
                        + "\n"
                        + " \n"
                        + "\t“All that matters on the chessboard is good moves.”\n"
                        + "― Bobby Fischer",
                "A propos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exit() {
        System.exit(0);
    }
}
