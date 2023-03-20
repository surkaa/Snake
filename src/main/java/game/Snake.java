package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snake implements Draw {

    private double angle;

    private Point head;

    // body 0 ==> (size - 1) ==> head
    private final List<Point> body;

    private boolean isAlive = true;

    //<editor-fold desc="构造函数">
    protected Snake(double angle, Point head, List<Point> body) {
        this.angle = angle;
        this.head = head;
        this.body = body;
    }

    protected Snake(double angle, Point head) {
        this(angle, head, new ArrayList<>());
    }

    protected Snake(double angle, Point head, Point... body) {
        this(angle, head, new ArrayList<>());
        Collections.addAll(this.body, body);
    }

    protected Snake(Point head) {
        this(0, head);
    }
    //</editor-fold>

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * 当下一个位置空旷时被调用
     */
    public void onMove() {
        final Point newHead = head.target(angle);
        body.add(head);
        head = newHead;
        body.remove(0);
    }

    /**
     * 当下一个位置与食物足够靠进的时被调用
     */
    public void onEat() {
        final Point newHead = head.target(angle);
        body.add(head);
        head = newHead;
    }

    @Override
    public void onDraw(Graphics g) {
        head.onDraw(g, Color.RED);
        for (Point point : body) {
            point.onDraw(g);
        }
    }

    public Point nextTarget() {
        return head.target(angle);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public double getAngle() {
        return angle;
    }

    public List<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return head;
    }

    public void die() {
        isAlive = false;
    }
}
