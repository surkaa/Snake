package game;

import java.awt.*;
import java.util.Objects;

/**
 * A point in the game.
 * 用于记录地图上的点
 *
 * @author Kaa
 */
public final class Point implements Draw {

    // 地图的最大宽度
    public static final int MAX_X = 64;

    // 地图的最大高度
    public static final int MAX_Y = 40;

    // 用于绘制的倍数（绘画时Point的坐标将乘以此倍数）
    public static final int MULTIPLE = 15;

    // 每次移动的距离
    public static final double MOVE_DISTANCE = 0.5;

    // 绘图的大小
    public static final int DRAW_SIZE_X = MAX_X * MULTIPLE;
    public static final int DRAW_SIZE_Y = MAX_Y * MULTIPLE;
    public static final int DRAW_RADIUS = 18;

    private final double x;

    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 生成一个随机的点
     *
     * @return a random point in the map
     */
    public static Point random() {
        return new Point(Math.random() * MAX_X, Math.random() * MAX_Y);
    }

    public static Point random(double d) {
        return new Point(Math.random() * (MAX_X - d * 2) + d, Math.random() * (MAX_Y - d * 2) + d);
    }

    /**
     * @return true if the point is out of the map
     */
    public boolean isBroken() {
        return x < 0 || x > MAX_X || y < 0 || y > MAX_Y;
    }

    /**
     * 通过方向获取目的地的点
     *
     * @param direction 目的地的方向
     * @return 目的地的点
     */
    public Point target(double direction) {
        double rad = Math.toRadians(direction % 360);
        double newX = x + Math.cos(rad) * MOVE_DISTANCE;
        double newY = y + Math.sin(rad) * MOVE_DISTANCE;
        return new Point(newX, newY);
    }

    /**
     * 检测与该点连线的角度
     * @param target 检测点
     * @return 角度
     */
    public double getAngle(Point target) {
        double dx = target.x - x;
        double dy = target.y - y;
        double rad = Math.atan2(dy, dx);
        return Math.toDegrees(rad);
    }

    /**
     * 检测是否与某点靠近
     * @param point 检测点
     * @param power 与MOVE_DISTANCE相乘的倍数，数值越大越容易返回true
     * @return 与point的距离是否小于power * MOVE_DISTANCE
     */
    public boolean isNear(Point point, double power) {
        double dx = point.x - x;
        double dy = point.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy) + 1e-3;
        return distance < MOVE_DISTANCE * power;
    }

    public boolean isNear(Point point) {
        return isNear(point, 1);
    }

    @Override
    public void onDraw(Graphics g) {
        draw(g, Color.BLACK);
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.fillOval((int) (x * MULTIPLE), (int) (y * MULTIPLE), DRAW_RADIUS, DRAW_RADIUS);
    }

    //<editor-fold desc="getter setter toString equals hashCode">
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    //</editor-fold>
}
