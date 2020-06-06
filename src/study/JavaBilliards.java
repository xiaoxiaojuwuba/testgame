package study;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:Java桌球演示
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * 
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */

public class JavaBilliards extends Panel implements Runnable, MouseListener,
        MouseMotionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -5019885590391523441L;

    // 球
    private double _ball;

    // 缓存用背景
    private Image _screen;

    // 画布
    private Graphics _graphics;

    // 台桌
    private Image _table;

    private double c[];

    private double d[];

    private double e[];

    private double f[];

    private int countBall;

    private int h;

    private double i[];

    private double j[];

    private double k[];

    private double l[];

    private double m[];

    private double n[];

    private double o[];

    private double p[];

    private boolean q[];

    private double r;

    private int a;

    private int u;

    private int v;

    private int w;

    private int x;

    private boolean y;

    private int z;

    private int A;

    /**
     * 初始化
     * 
     */
    public JavaBilliards() {
        a = 0;
        r = 10D;
        z = 300;
        A = 0;
        setBounds(50, 50, 700, 350);
        Frame frame = new Frame("Java桌球演示");
        frame.add(this);
        frame.setBounds(0, 0, 700, 380);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        requestFocus();
        initialize();
    }

    public void initialize() {
        // 基础数据
        base();
        // 注入实例
        immit();
        // 缓存背景
        _screen = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        // 背景图形
        _graphics = _screen.getGraphics();
        // 绘制台桌
        makeTable();
        // 设置监听
        addMouseListener(this);
        addMouseMotionListener(this);
        // 启动
        new Thread(this).start();
    }

    /**
     * 初始化数据
     * 
     */
    public void base() {

        // 球体
        _ball = 16D;
        c = (new double[] { 40D, (double) (getWidth() - 40) });
        d = (new double[] { c[0], (double) getHeight() - c[0] });
        e = (new double[] { c[0] + 20D, (double) (getWidth() / 2), c[1] - 20D });
        f = (new double[] { d[0] + 20D, d[1] - 20D });
    }

    /**
     * 注入实例
     * 
     */
    public void immit() {
        countBall = 16;
        i = new double[countBall];
        j = new double[countBall];
        k = new double[countBall];
        l = new double[countBall];
        m = new double[countBall];
        n = new double[countBall];
        o = new double[countBall];
        p = new double[countBall];
        q = new boolean[countBall];
        // 打击对象
        hitObject();
        // 打击用球
        hitBall();
    }

    /**
     * 打击用球数值
     * 
     */
    public void hitBall() {
        i[0] = (1.0D * (e[2] - e[0])) / 3D;
        j[0] = this.getHeight() / 2;
        k[0] = 0.0D;
        l[0] = 0.0D;
        q[0] = true;
    }

    /**
     * 打击对象
     * 
     */
    public void hitObject() {
        int il = 1;
        h = countBall - 1;
        // 求平方根
        double dl = Math.sqrt(3.5D);
        for (int j1 = 0; j1 < 5; j1++) {
            double d2 = ((double) getWidth() * 2D) / 3D + (double) j1 * dl * r;
            double d3 = (double) (getHeight() / 2) - (double) j1 * r;
            for (int k1 = 0; k1 <= j1; k1++) {
                i[il] = d2;
                j[il] = d3;
                k[il] = 0.0D;
                l[il] = 0.0D;
                q[il] = true;
                d3 += 2D * r;
                il++;
            }

        }

    }
    
    /**
     * 绘制台球桌
     * 
     */
    public void makeTable() {
        _table = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        Graphics g = _table.getGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor((new Color(200, 100, 50)).darker());
        g.fill3DRect((int) c[0], (int) d[0], (int) (c[1] - c[0]),
                (int) (d[1] - d[0]), true);
        g.setColor(Color.BLACK);
        g.fill3DRect((int) e[0], (int) f[0], (int) (e[2] - e[0]),
                (int) (f[1] - f[0]), false);
        g.setColor(Color.GREEN.darker());
        g.drawLine((int) ((1.0D * (e[2] - e[0])) / 3D), (int) f[0],
                (int) ((1.0D * (e[2] - e[0])) / 3D), (int) f[1]);
        g.fillOval((int) ((1.0D * (e[2] - e[0])) / 3D) - 2,
                (int) ((f[1] + f[0]) / 2D) - 2, 4, 4);
        g.drawArc((int) ((1.0D * (e[2] - e[0])) / 3D) - 20,
                (int) ((f[1] + f[0]) / 2D) - 20, 40, 40, 90, 180);
        g.setColor(Color.BLACK);
        double d1 = _ball - 2D;
        for (int i1 = 0; i1 < 3; i1++) {
            for (int j1 = 0; j1 < 2; j1++) {
                g.fillOval((int) (e[i1] - d1), (int) (f[j1] - d1),
                        (int) (2D * d1), (int) (2D * d1));
            }
        }
    }

    /**
     * 线程处理
     */
    public void run() {
        long timeStart;
        timeStart = System.currentTimeMillis();
        // 死循环反复处理
        for (;;) {
            long timeEnd = System.currentTimeMillis();
            switch (a) {
            default:
                break;

            case 1:
                // 根据时间换算运动轨迹
                conversion(timeEnd - timeStart);
                // 过程处理
                course();
                break;

            case 2:
                conversion(timeEnd - timeStart);
                // 过程处理
                course();
                boolean flag = true;
                for (int i1 = 0; flag && i1 < countBall; i1++)
                    flag = k[i1] == 0.0D && l[i1] == 0.0D;

                if (flag) {
                    a = 1;
                    // 击球
                    if (!q[0]) {
                        hitBall();
                    }
                }
                if (h == 0)
                    a = 3;
                break;

            case 3:
                hitObject();
                hitBall();
                a = 0;
                break;
            }

            repaint();
            timeStart = timeEnd;
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void course() {
        // 限制区域
        limit();
        // 入袋处理
        pocket();
        // 运动演算
        play();
        for (int i1 = 0; i1 < countBall; i1++)
            if (q[i1]) {
                i[i1] = m[i1];
                j[i1] = n[i1];
            }
    }

    /**
     * 变换时间为动作数据
     * 
     * @param value
     */
    public void conversion(long value) {
        double d1 = (double) value / 1000D;
        for (int i1 = 0; i1 < countBall; i1++)
            if (q[i1]) {
                m[i1] = i[i1] + k[i1] * d1;
                n[i1] = j[i1] + l[i1] * d1;
                k[i1] *= 0.98999999999999999D;
                l[i1] *= 0.98999999999999999D;
                if (Math.abs(Math.hypot(k[i1], l[i1])) < 2D) {
                    k[i1] = 0.0D;
                    l[i1] = 0.0D;
                }
            }

    }

    public void pocket() {
        for (int i1 = 0; i1 < countBall; i1++)
            if (q[i1]) {
                for (int j1 = 0; j1 < 3; j1++) {
                    for (int k1 = 0; k1 < 2; k1++)
                        if (Math.hypot(e[j1] - i[i1], f[k1] - j[i1]) < _ball) {
                            q[i1] = false;
                            if (i1 != 0) {
                                h--;
                            }
                            k[i1] = 0.0D;
                            l[i1] = 0.0D;
                        }

                }

            }

    }

    public void play() {
        for (int i1 = 0; i1 < countBall; i1++)
            if (q[i1]) {
                for (int j1 = i1 + 1; j1 < countBall; j1++) {
                    boolean flag;
                    if (q[j1] && (flag = randball(i1, j1))) {
                        for (int k1 = 0; k1 < 10 && flag; k1++) {
                            m[i1] = (m[i1] + i[i1]) / 2D;
                            n[i1] = (n[i1] + j[i1]) / 2D;
                            m[j1] = (m[j1] + i[j1]) / 2D;
                            n[j1] = (n[j1] + j[j1]) / 2D;
                            flag = randball(i1, j1);
                        }

                        if (flag) {
                            m[i1] = i[i1];
                            n[i1] = j[i1];
                            m[j1] = i[j1];
                            n[j1] = j[j1];
                        }
                        double d1 = m[j1] - m[i1];
                        double d2 = n[j1] - n[i1];
                        double d3 = Math.hypot(m[i1] - m[j1], n[i1] - n[j1]);
                        double d4 = d1 / d3;
                        double d5 = d2 / d3;
                        o[j1] = k[j1] - k[j1] * d4 * d4;
                        o[j1] -= l[j1] * d4 * d5;
                        o[j1] += k[i1] * d4 * d4;
                        o[j1] += l[i1] * d4 * d5;
                        p[j1] = l[j1] - l[j1] * d5 * d5;
                        p[j1] -= k[j1] * d4 * d5;
                        p[j1] += k[i1] * d4 * d5;
                        p[j1] += l[i1] * d5 * d5;
                        o[i1] = k[i1] - k[i1] * d4 * d4;
                        o[i1] -= l[i1] * d4 * d5;
                        o[i1] += k[j1] * d4 * d4;
                        o[i1] += l[j1] * d4 * d5;
                        p[i1] = l[i1] - l[i1] * d5 * d5;
                        p[i1] -= k[i1] * d4 * d5;
                        p[i1] += k[j1] * d4 * d5;
                        p[i1] += l[j1] * d5 * d5;
                        k[i1] = o[i1];
                        l[i1] = p[i1];
                        k[j1] = o[j1];
                        l[j1] = p[j1];
                    }
                }

            }

    }

    public boolean randball(int i1, int j1) {
        // hypot随机决定两值之一
        return Math.hypot(m[i1] - m[j1], n[i1] - n[j1]) < 2D * r;
    }

    /**
     * 限制区域
     * 
     */
    public void limit() {
        for (int i = 0; i < countBall; i++)
            if (q[i]) {
                if (m[i] - r < e[0]) {
                    m[i] = e[0] + r;
                    k[i] *= -1D;
                } else if (m[i] + r > e[2]) {
                    m[i] = e[2] - r;
                    k[i] *= -1D;
                }
                if (n[i] - r < f[0]) {
                    n[i] = f[0] + r;
                    l[i] *= -1D;
                } else if (n[i] + r > f[1]) {
                    n[i] = f[1] - r;
                    l[i] *= -1D;
                }
            }

    }

    public void makeScreen(Graphics screenGraphics) {

        screenGraphics.drawImage(_table, 0, 0, null);
        if (q[0]) {
            _graphics.setColor(Color.WHITE);
            _graphics.fillOval((int) (i[0] - r), (int) (j[0] - r),
                    (int) (r * 2D), (int) (r * 2D));
        }
        screenGraphics.setColor(Color.RED);
        for (int i1 = 1; i1 < countBall; i1++)
            if (q[i1])
                screenGraphics.fillOval((int) (i[i1] - r), (int) (j[i1] - r),
                        (int) (r * 2D), (int) (r * 2D));

        screenGraphics.setColor(Color.BLACK);
        for (int j1 = 0; j1 < countBall; j1++)
            if (q[j1]) {
                screenGraphics.drawOval((int) (i[j1] - r), (int) (j[j1] - r),
                        (int) (r * 2D), (int) (r * 2D));
            }

        if (a == 1){
            makeHelper(screenGraphics);
        }
        if (a == 0) {
            int k1 = getWidth() / 2 - 85;
            int l1 = getHeight() / 2;
            screenGraphics.setColor(Color.BLACK);
            screenGraphics.drawString("点击画面开始", k1 + 2, l1 + 2);
            if ((System.currentTimeMillis() / 1000L & 1L) == 0L){
                screenGraphics.setColor(Color.YELLOW);
            }
            else{
                screenGraphics.setColor(Color.CYAN);
            }
            screenGraphics.drawString("点击画面开始", k1, l1);
        }
    }

    /**
     * 绘制球杆及辅助线
     * 
     * @param screenGraphics
     */
    public void makeHelper(Graphics screenGraphics) {
        double d1 = Math.hypot(i[0] - (double) u, j[0] - (double) v);
        double d2 = ((double) u - i[0]) / d1;
        double d3 = ((double) v - j[0]) / d1;
        double d4 = y ? n() / 10D : 1.0D;
        double d5 = i[0] + d2 * (r + d4);
        double d6 = i[0] + d2 * (r + (double) z + d4);
        double d7 = j[0] + d3 * (r + d4);
        double d8 = j[0] + d3 * (r + (double) z + d4);
        screenGraphics.setColor(Color.ORANGE);
        screenGraphics.drawLine((int) d5, (int) d7, (int) d6, (int) d8);
        int i1 = 0;
        int j1 = y ? (int) (150D * (d4 / 1000D)) : 15;
        double d9;
        double d10 = (d9 = 30D) * d2;
        double d11 = d9 * d3;
        double d12 = i[0] + (double) A * d2;
        double d13 = j[0] + (double) A * d3;
        A--;
        A %= d9;
        screenGraphics.setColor(Color.WHITE);
        for (; i1 < j1; i1++) {
            if (d12 < e[0]) {
                d12 = e[0] - d12;
                d12 = e[0] + d12;
                d10 *= -1D;
            } else if (d12 > e[2]) {
                d12 -= e[2];
                d12 = e[2] - d12;
                d10 *= -1D;
            }
            if (d13 < f[0]) {
                d13 = f[0] - d13;
                d13 = f[0] + d13;
                d11 *= -1D;
            } else if (d13 > f[1]) {
                d13 -= f[1];
                d13 = f[1] - d13;
                d11 *= -1D;
            }
            screenGraphics.fillOval((int) d12 - 2, (int) d13 - 2, 4, 4);
            d12 -= d10;
            d13 -= d11;
        }

    }

    public double n() {
        if (y) {
            return Math.min(1000D, 10D * Math.hypot(i[0] - (double) w, j[0]
                    - (double) x));
        } else {
            return Math.min(1000D, 10D * Math.hypot(u - w, v - x));
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        makeScreen(_graphics);
        g.drawImage(_screen, 0, 0, null);
    }

    public void mousePressed(MouseEvent mouseevent) {
        y = true;
    }

    public void mouseReleased(MouseEvent mouseevent) {
        if (a == 1) {
            double d1 = Math.hypot(i[0] - (double) u, j[0] - (double) v);
            double d2 = (i[0] - (double) u) / d1;
            double d3 = (j[0] - (double) v) / d1;
            double d4;
            if ((d4 = n()) > 0.0D) {
                a = 2;
                k[0] = d4 * d2;
                l[0] = d4 * d3;
            }
        }
        y = false;
    }

    public void mouseClicked(MouseEvent mouseevent) {
        if (a == 0)
            a = 1;
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mouseMoved(MouseEvent mouseevent) {
        w = mouseevent.getX();
        x = mouseevent.getY();
        u = w;
        v = x;
    }

    public void mouseDragged(MouseEvent mouseevent) {
        w = mouseevent.getX();
        x = mouseevent.getY();
    }

    public static void main(String args[]) {
        new JavaBilliards();
    }

}
