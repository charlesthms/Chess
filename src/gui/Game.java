package gui;

import engine.Board;

import java.awt.*;

public class Game implements Runnable {

    public static final int DEFAULT_TILE_SIZE = 32;
    public static final int TILES = 8;
    public static final float SCALE = 2.8f;
    public static final int TILES_SIZE = (int) (DEFAULT_TILE_SIZE * SCALE);
    public static final int WIDTH = TILES_SIZE * TILES + 60;
    public static final int HEIGHT = WIDTH;
    public static final int OFFSET = 30;

    private Thread gameThread;
    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Board boardManager;

    private final int MAX_FPS = 60;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel, this);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        boardManager = new Board();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void render(Graphics g) {
        boardManager.draw(g, gamePanel);
    }

    public void update() {
        boardManager.update();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / MAX_FPS;
        long lastFrame = System.nanoTime();
        long now;

        int frames= 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {
            now = System.nanoTime();

            if (now - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                update();

                lastFrame = now;
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    public Board getBoardManager() {
        return boardManager;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
