package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snake implements Draw {

    private double angle;

    private Point head;

    // tail 0 ==> (size - 1) ==> head
    private final List<Point> tail;

    private boolean isAlive = true;

    protected Snake(double angle, Point head, List<Point> tail) {
        this.angle = angle;
        this.head = head;
        this.tail = tail;
    }

    protected Snake(double angle, Point head) {
        this(angle, head, new ArrayList<>());
    }

    protected Snake(double angle, Point head, Point... tail) {
        this(angle, head, new ArrayList<>());
        Collections.addAll(this.tail, tail);
    }

    protected Snake(Point head) {
        this(0, head);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * 当下一个位置空旷时被调用
     */
    public void onMove() {
        final Point newHead = head.target(angle);
        tail.add(head);
        head = newHead;
        tail.remove(0);
    }

    /**
     * 当下一个位置与食物足够靠进的时被调用
     */
    public void onEat() {
        final Point newHead = head.target(angle);
        tail.add(head);
        head = newHead;
    }

    @Override
    public void onDraw(Graphics g) {
        head.onDraw(g, Color.RED);
        for (Point point : tail) {
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

    public List<Point> getTail() {
        return tail;
    }

    public Point getHead() {
        return head;
    }

    public void die() {
        isAlive = false;
    }
}
