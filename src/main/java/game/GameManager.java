package game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class GameManager implements Draw, KeyListener {

    private final List<Snake> snakes = new ArrayList<>();

    private final List<Food> foods = new ArrayList<>();

    // 运行监听器列表 用于监听每次运行前后（step函数）的事件
    private final List<RunListener> runListeners = new ArrayList<>();

    // 装入Thread的Runnable
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 只在isPause为false时运行
            while (!isPause) {
                step();
            }
        }
    };

    // 每次运行的间隔时间
    private int sleepTime = 50;

    // 是否暂停
    private boolean isPause = false;

    //<editor-fold desc="单例构造">
    /**
     * 单例模式
     */
    private static GameManager INSTANCE = null;

    // 私有构造函数 用于单例模式 不能直接new
    private GameManager() {

    }

    /**
     * 单例模式
     */
    public static GameManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }
    //</editor-fold>

    //<editor-fold desc="RunListener">
    /**
     * 运行监听器
     */
    public interface RunListener {
        /**
         * 在每次运行前调用
         */
        void beforeRun();

        /**
         * 在每次运行后调用
         */
        void afterRun();
    }

    public void addRunListener(RunListener runListener) {
        runListeners.add(runListener);
    }

    public void removeRunListener(RunListener runListener) {
        runListeners.remove(runListener);
    }

    public void clearRunListener() {
        runListeners.clear();
    }
    //</editor-fold>

    // 开始或继续游戏
    public void start() {
        isPause = false;
        new Thread(runnable).start();
    }

    // 暂停游戏
    private void pause() {
        isPause = true;
    }

    /**
     * 一次运行, 所有蛇都走一步
     */
    public void step() {
        for (RunListener runListener : runListeners) {
            runListener.beforeRun();
        }
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                runOneSnake(snake);
            }
        }
        for (RunListener runListener : runListeners) {
            runListener.afterRun();
        }
        sleep();
    }

    // 轮到此蛇走一步
    private void runOneSnake(Snake snake) {
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

    private void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //<editor-fold desc="检测蛇状态">
    // 找到被吃掉的食物并新增食物
    private void replaceFood(Point next) {
        // 删除食物 TODO 优化删除方式
        foods.removeIf(
                food -> food.isNear(next)
        );
        // 生成新食物
        newFood(1);
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
                return true; // TODO 优化 直接返回食物
            }
        }
        return false;
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
    //</editor-fold>

    //<editor-fold desc="new蛇">
    /**
     * 新建蛇
     * @param angle 初始角度
     * @param head 初始头部位置
     * @return 新建的蛇
     */
    public Snake newSnake(double angle, Point head) {
        Snake snake = new Snake(angle, head);
        snakes.add(snake);
        return snake;
    }

    /**
     * 新建蛇 (默认角度为0)
     * @param head 初始头部位置
     * @return 新建的蛇
     */
    public Snake newSnake(Point head) {
        Snake snake = new Snake(head);
        snakes.add(snake);
        return snake;
    }

    /**
     * 新建蛇 只在地图右上角生成
     * @return 新建的蛇
     */
    public Snake newSnake() {
        double x = Point.MAX_X / 4.0;
        double y = Point.MAX_Y / 4.0;
        double dx = Point.MOVE_DISTANCE;
        List<Point> tail = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            tail.add(new Point(x - dx * i, y));
        }
        Snake snake = new Snake(0.0, new Point(x, y), tail);
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

    public void newFood(int count) {
        for (int i = 0; i < count; i++){
            Food food = new Food();
            foods.add(food);
        }
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

    //<editor-fold desc="KeyListener">
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
                break;
            case KeyEvent.VK_CONTROL:
            case KeyEvent.VK_DOWN:
                sleepTime = 150;
                break;
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_UP:
                sleepTime = 10;
                break;
            case KeyEvent.VK_SPACE:
                if (isPause) start();
                else pause();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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

    @Override
    public void keyTyped(KeyEvent e) {
    }
    //</editor-fold>
}
