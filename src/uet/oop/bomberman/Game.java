package uet.oop.bomberman;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.input.KeyBoardSpe;
import uet.oop.bomberman.input.KeyBoardSpe2;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.sound.Sound;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas {


    public static final int TILES_SIZE = 16;
    public static final int WIDTH = TILES_SIZE * (31 / 2);
    public static final int HEIGHT = 13 * TILES_SIZE;
    public static final String TITLE = "BombermanGame";
    public static final int TIME = 200;
    public static final int POINTS = 0;
    private static final int BOMBRATE = 1;
    private static final int BOMBRADIUS = 1;
    private static final double BOMBERSPEED = 1.0;//toc do bomber
    public static int SCALE = 3;
    protected static int SCREENDELAY = 3;

    protected static int bombRate = BOMBRATE;
    protected static int bombRadius = BOMBRADIUS;
    protected static double bomberSpeed = BOMBERSPEED;

    protected static int bombRate2 = BOMBRATE;
    protected static int bombRadius2 = BOMBRADIUS;
    protected static double bomberSpeed2 = BOMBERSPEED;


    protected int _screenDelay = SCREENDELAY;

    private final KeyBoardSpe _input;
    private final KeyBoardSpe2 _input2;
    private boolean _running = false;
    private boolean _paused = true;

    private final Board _board;
    private final Screen screen;
    private final Frame _frame;

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Game(Frame frame) {
        _frame = frame;
        _frame.setTitle(TITLE);

        screen = new Screen(WIDTH, HEIGHT);
        _input = new KeyBoardSpe();
        _input2 = new KeyBoardSpe2();

        _board = new Board(this, _input, _input2, screen);
        addKeyListener(_input);
        addKeyListener(_input2);
    }

    public static double getBomberSpeed() {
        return bomberSpeed;
    }

    public static void setBomberSpeed(double bomberSpeed) {
        Game.bomberSpeed = bomberSpeed;
    }

    public static int getBombRate() {
        return bombRate;
    }

    public static void setBombRate(int bombRate) {
        Game.bombRate = bombRate;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static void setBombRadius(int bombRadius) {
        Game.bombRadius = bombRadius;
    }

    public static void addBomberSpeed(double i) {
        bomberSpeed += i;
    }

    public static void addBombRadius(int i) {
        bombRadius += i;
    }

    public static void addBombRate(int i) {
        bombRate += i;
    }

    public static double getBomberSpeed2() {
        return bomberSpeed2;
    }

    public static void setBomberSpeed2(double bomberSpeed) {
        Game.bomberSpeed2 = bomberSpeed;
    }

    public static int getBombRate2() {
        return bombRate2;
    }

    public static void setBombRate2(int bombRate) {
        Game.bombRate2 = bombRate;
    }

    public static int getBombRadius2() {
        return bombRadius2;
    }

    public static void setBombRadius2(int bombRadius) {
        Game.bombRadius2 = bombRadius;
    }

    public static void addBomberSpeed2(double i) {
        bomberSpeed2 += i;
    }

    public static void addBombRadius2(int i) {
        bombRadius2 += i;
    }

    public static void addBombRate2(int i) {
        bombRate2 += i;
    }

    private void renderGame() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        _board.render(screen);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen._pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        _board.renderMessages(g);

        g.dispose();
        bs.show();
    }

    private void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        Graphics g = bs.getDrawGraphics();

        _board.drawScreen(g);

        g.dispose();
        bs.show();
    }

    private void update() {
        _input.update();
        _input2.update();
        _board.update();
    }

    public void start() {
        _running = true;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0; //nanosecond, 60 frames per second
        double delta = 0;
        int frames = 0;
        int updates = 0;
        requestFocus();
        while (_running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            if (_paused) {
                if (_screenDelay <= 0) {
                    _board.setShow(-1);
                    _paused = false;
                }

                renderScreen();
            } else {
                renderGame();
            }


            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                _frame.setTime(_board.subtractTime());
                _frame.setPoints(_board.getPoints());
                timer += 1000;
                _frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
                updates = 0;
                frames = 0;

                if (_board.getShow() == 2)
                    --_screenDelay;
            }
        }
    }

    public void resetScreenDelay() {
        _screenDelay = SCREENDELAY;
    }

    public Board getBoard() {
        return _board;
    }

    public boolean isPaused() {
        return _paused;
    }

    public void pause() {
        _paused = true;
    }

    public void run() {
        _running = true;
        _paused = false;
    }
}
