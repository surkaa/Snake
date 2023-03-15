package ui;

import game.GameManager;
import game.Point;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public final class View extends JPanel {

    private final GameManager manager = GameManager.getINSTANCE();

    public View() {
        super();
        addMouseListener(manager);
        addMouseMotionListener(manager);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 画背景
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y);
        manager.onDraw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y);
    }
}
