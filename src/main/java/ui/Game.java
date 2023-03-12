package ui;

import game.Field;

import javax.swing.JFrame;

public final class Game extends JFrame {

    private final Field field = Field.getINSTANCE();
    private final View view = new View();

    public Game(String title) {
        // 设置标题
        super(title);
        // 关闭窗口后程序结束
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口大小不可变
        setResizable(false);
        // 添加视图
        add(view);
        // 自动调整窗口大小 根据view的大小
        pack();
        // 显示窗口
        setVisible(true);
        // 窗口居中
        setLocationRelativeTo(null);
        // 添加键盘监听
        addKeyListener(this.field);
        // 添加field运行监听
        field.addRunListener(new Field.RunListener() {
            @Override
            public void beforeRun() {
                view.repaint();
            }

            @Override
            public void afterRun() {

            }
        });
    }

    public static void main(String[] args) {
        Game game = new Game("Snake");
        game.field.newSnake();
        game.field.newFood(5);
        game.field.start();
    }
}
