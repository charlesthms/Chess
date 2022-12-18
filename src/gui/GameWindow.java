package gui;

import engine.Board;
import engine.moves.Move;
import engine.parser.Fen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

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
    private JMenuItem itemPrintMoves;

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
        itemNew.addActionListener(l -> {
            game.setBoardManager(new Board(Fen.DEFAULT_FEN));
        });

        this.itemSave = new JMenuItem("Sauvegarder");
        gameMenu.add(itemSave);
        itemSave.addActionListener(l -> {
            saveGameDialog();
        });
        this.itemLoad = new JMenuItem("Charger");
        gameMenu.add(itemLoad);
        gameMenu.addSeparator();
        itemLoad.addActionListener(l -> {
            loadGameDialog();
        });

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

        this.itemPrintMoves = new JMenuItem("Afficher les mouvements possibles");
        debugMenu.add(itemPrintMoves);
        itemPrintMoves.addActionListener(actionEvent -> {
            for (Move m : game.getBoardManager().generateMoves()) {
                System.out.println(m);
            }
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

    private void saveGameDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder la partie");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("FEN format files", "fen");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".fen")) {
                file = new File(file.getParentFile(), file.getName() + ".fen");
            }
            try(FileWriter fw = new FileWriter(file)) {
                fw.write(Fen.getFen(game.getBoardManager()));
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadGameDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger la partie");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("FEN format files", "fen");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".fen")) {
                file = new File(file.getParentFile(), file.getName() + ".fen");
            }

            try (FileReader fr = new FileReader(file)) {
                StringBuilder fen = new StringBuilder();
                int i;
                while((i=fr.read())!=-1)
                    fen.append((char) i);

                game.setBoardManager(new Board(fen.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exit() {
        System.exit(0);
    }
}
