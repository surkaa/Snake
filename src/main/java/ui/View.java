package ui;

import game.Field;
import game.Point;
import game.Snake;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class View extends JPanel {

    private final Field field = Field.getINSTANCE();

    private static int a = 0;

    public View() {
        super();
        addMouseListener(new MouseAdapter() {
            // 1.mouseClicked(MouseEvent e)     //鼠标按键在组件上单击（按下并释放）时调用。
            // 2.mouseEntered(MouseEvent e)     /鼠标进入到组件上时调用。
            // 3.mouseExited(MouseEvent e)      //鼠标离开组件时调用。
            // 4.mousePressed(MouseEvent e)     //鼠标按键在组件上按下时调用。
            // 5.mouseReleased(MouseEvent e)    //鼠标按钮在组件上释放时调用。
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // 只有一个蛇时，点击鼠标左键，蛇向鼠标所在位置移动
                Snake snake = field.isOneSnake();
                a++;
                if (snake != null) {
                    Point head = snake.getHead();
                    Point target = new Point(e.getX(), e.getY());
                    double newAngle = head.getAngle(target);
                    snake.setAngle(newAngle);
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 画背景
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y);
        field.onDraw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y);
    }
}
