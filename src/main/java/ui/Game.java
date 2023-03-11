package ui;

import game.Field;
import game.Point;

import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game extends JFrame {

    private final Field field = Field.getINSTANCE();
    private final View view;

    private long sleepTime = 100;

    public Game(String title, View view) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.view = view;
        add(Game.this.view);
        pack();
        setVisible(true);
        addKeyListener(new KeyAdapter() {
            // 空格键加速
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 32) {
                    sleepTime = 20;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == 32) {
                    sleepTime = 100;
                }
            }
        });
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
