package ui;

import game.Field;
import game.Point;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JFrame implements KeyListener {

    private final Field field = Field.getINSTANCE();
    private final View view;
    private long sleepTime = 50;
    private final SnakeRunnable runnable = new SnakeRunnable();

    class SnakeRunnable implements Runnable {
        boolean pause = true;

        @Override
        public void run() {
            while (pause) {
                runOnce();
            }
        }
    }

    public Game(String title, View view) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.view = view;
        add(Game.this.view);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        addKeyListener(this);
        addKeyListener(this.field);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // shift/up键加速 ctrl/down键减速 空格暂停继续
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_DOWN:
                sleepTime = 150;
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_UP:
                sleepTime = 10;
                break;
            case KeyEvent.VK_SPACE:
                resumeOrPause();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                sleepTime = 50;
                break;
            default:
                break;
        }
    }

    private void start() {
        runnable.pause = true;
        new Thread(runnable).start();
    }

    private void resumeOrPause() {
        if (runnable.pause) {
            runnable.pause = false;
        } else {
            start();
        }
    }

    private void runOnce() {
        field.run();
        view.repaint();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game = new Game("Snake", new View());
        game.field.newSnake(0, new Point(0.0, 0.0));
        game.field.newFood();
        game.start();
    }
}
