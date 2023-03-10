package ui;

import game.Field;
import game.Point;

import javax.swing.JFrame;

public class Game extends JFrame {

    private final Field field = Field.getINSTANCE();
    private final View view;

    public Game(String title, View view) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.view = view;
        add(Game.this.view);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        Game game = new Game("Snake", new View());
        Field field = Field.getINSTANCE();
        field.newSnake(0, new Point(30.0, 30.0));
        field.newFood(new Point(10.0, 0.0));
        field.newFood(new Point(13.0, 0.0));
        field.newFood(new Point(16.0, 0.0));
        field.newFood(new Point(19.0, 0.0));
        int i = 0;
        while (i++ < 10000000) {
            game.run();
        }
    }

    private void run() {
        field.run();
        view.repaint();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
