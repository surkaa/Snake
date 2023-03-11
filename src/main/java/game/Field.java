package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class Field implements Draw, KeyListener {

    private final List<Snake> snakes = new ArrayList<>();

    private final List<Food> foods = new ArrayList<>();

    //<editor-fold desc="单例构造">
    /**
     * 单例模式
     */
    private static Field INSTANCE = null;

    private Field() {

    }

    /**
     * 单例模式
     */
    public static Field getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Field();
        }
        return INSTANCE;
    }
    //</editor-fold>

    public void run() {
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                once(snake);
            }
        }
    }

    private void once(Snake snake) {
        Point next = snake.nextTarget();
        if (checkFood(next)) {
            snake.onEat();
            replaceFood(next);
        } else if (checkWall(next)) {
            snake.die();
            System.out.println("die wall");
        } else if (checkSnake(snake, next)) {
            snake.die();
            System.out.println("die self");
        } else if (checkOtherSnake(snake, next)) {
            snake.die();
            System.out.println("die other snake");
        } else {
            snake.onMove();
        }
        // TODO 此函数运行耗时很大，需要优化
        // TODO 可以写一个Result类，用于存储结果，用于once中蛇的动作
    }

    private void replaceFood(Point next) {
        // 删除食物
        foods.removeIf(
                new Predicate<Food>() {
                    @Override
                    public boolean test(Food food) {
                        return food.isNear(next);
                    }
                }
        );
        // 生成新食物
        newFood();
    }

    /**
     * 检查是否撞到其他蛇（不包含自身）
     *
     * @param self 自身
     * @param next 下一步的位置
     * @return 是否撞到
     */
    public boolean checkOtherSnake(Snake self, Point next) {
        for (Snake other : snakes) {
            if (other != self) {
                return checkSnake(other, next);
            }
        }
        return false;
    }

    /**
     * 检查是否撞到此蛇（可以是自身）
     *
     * @param snake 蛇
     * @param next  下一步的位置
     * @return 是否撞到
     */
    public boolean checkSnake(Snake snake, Point next) {
        if (snake.getHead().isNear(next, 0.5)) {
            return true;
        }
        for (Point point : snake.getTail()) {
            if (point.isNear(next, 0.5)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否撞到墙
     *
     * @param next 下一步的位置
     * @return 是否撞到
     */
    public boolean checkWall(Point next) {
        return next.isBroken();
    }

    /**
     * 检查是否撞到食物
     *
     * @param next 下一步的位置
     * @return 是否撞到
     */
    public boolean checkFood(Point next) {
        for (Food food : foods) {
            if (food.isNear(next)) {
                return true;
            }
        }
        return false;
    }

    //<editor-fold desc="new蛇">
    public Snake newSnake(double angle, Point head) {
        Snake snake = new Snake(angle, head);
        snakes.add(snake);
        return snake;
    }

    public Snake newSnake(Point head) {
        Snake snake = new Snake(head);
        snakes.add(snake);
        return snake;
    }
    //</editor-fold>

    //<editor-fold desc="new食物">
    public Food newFood(Point point, int value, Color color) {
        Food food = new Food(point, value);
        foods.add(food);
        return food;
    }

    public Food newFood(Point point, int value) {
        Food food = new Food(point, value);
        foods.add(food);
        return food;
    }

    public Food newFood(Point point) {
        Food food = new Food(point);
        foods.add(food);
        return food;
    }

    public Food newFood() {
        Food food = new Food();
        foods.add(food);
        return food;
    }
    //</editor-fold>

    @Override
    public void onDraw(Graphics g) {
        for (Snake snake : snakes) {
            snake.onDraw(g);
        }
        for (Food food : foods) {
            food.onDraw(g);
        }
    }

    /**
     * 是否只剩下一条蛇
     *
     * @return 是否只剩下一条蛇, 如果是，返回此蛇， 否则返回null
     */
    public Snake isOneSnake() {
        if (snakes.size() == 1) {
            return snakes.get(0);
        }
        return null;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Snake snake = isOneSnake();
        if (snake == null) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D:
                snake.setAngle(0);
                break;
            case KeyEvent.VK_S:
                snake.setAngle(90);
                break;
            case KeyEvent.VK_A:
                snake.setAngle(180);
                break;
            case KeyEvent.VK_W:
                snake.setAngle(270);
                break;
            case KeyEvent.VK_LEFT:
                snake.setAngle(snake.getAngle() - 10);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setAngle(snake.getAngle() + 10);
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
