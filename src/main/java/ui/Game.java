package ui;

import game.Field;
import game.Point;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {

    private final Field field = Field.getINSTANCE();
    private final View view;
    private boolean pause = true;
    private long sleepTime = 50;

    public Game(String title, View view) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.view = view;
        add(Game.this.view);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        addKeyListener(new KeyAdapter() {
            // shift/up键加速 ctrl/down键减速
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
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
                        pause = !pause;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
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
        });
        addKeyListener(this.field);
    }

    private void start() {
        new Thread(() -> {
            while (true) {
                if (pause) {
                    run();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.exit(0);
                    }
                }
            }
        }).start();
    }

    private void run() {
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
        Field field = Field.getINSTANCE();
        field.newSnake(0, new Point(0.0, 0.0));
        field.newFood();
        game.start();
    }
}
