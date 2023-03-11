package ui;

import game.Field;
import game.Point;

import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {

    private final Field field = Field.getINSTANCE();
    private final View view;

    private long sleepTime = 50;

    public Game(String title, View view) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.view = view;
        add(Game.this.view);
        pack();
        setVisible(true);
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

    public static void main(String[] args) {
        Game game = new Game("Snake", new View());
        Field field = Field.getINSTANCE();
        field.newSnake(0, new Point(0.0, 0.0));
        field.newFood(new Point(10.0, 10.0));
        int i = 0;
        while (i++ < 10000000) {
            game.run();
        }
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
}
