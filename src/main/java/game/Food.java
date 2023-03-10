package game;

import java.awt.*;

public class Food implements Draw{

    private Point point;

    private int value;

    private Color color;

    public Food(Point point, int value, Color color) {
        this.point = point;
        this.value = value;
        this.color = color;
    }

    public Food(Point point, int value) {
        this(point, value, Color.YELLOW);
    }

    public Food(Point point) {
        this(point, 1);
    }

    public Food() {
        this(Point.random());
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void onDraw(Graphics g) {
        g.setColor(color);
        point.onDraw(g);
    }
}
