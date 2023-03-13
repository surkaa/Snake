package ui;

import game.GameManager;

import javax.swing.JFrame;

public final class GameFrame extends JFrame {

    private final GameManager manager = GameManager.getINSTANCE();
    private final View view = new View();

    public GameFrame(String title) {
        // 设置标题
        super(title);
        // 关闭窗口后程序结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口大小不可变
        this.setResizable(false);
        // 添加视图
        this.add(view);
        // 自动调整窗口大小 根据view的大小
        this.pack();
        // 显示窗口
        this.setVisible(true);
        // 窗口居中
        this.setLocationRelativeTo(null);
        // 添加键盘监听
        this.addKeyListener(this.manager);
        // 添加field运行监听
        manager.addRunListener(new GameManager.RunListener() {
            @Override
            public void beforeRun() {
                view.repaint();
            }

            @Override
            public void afterRun() {

            }
        });
    }

    public void defaultStart() {
        manager.newFood(6);
        manager.newSnake();
        manager.start();
    }

    public static void main(String[] args) {
        new GameFrame("Snake").defaultStart();
    }
}
