package study.learning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;//创建窗口用的类
import java.awt.Graphics;//是一个用来绘制2D图像必须导入的java包，提供对图形图像的像素，颜色的绘制。
import java.awt.Graphics2D;
import java.awt.Image;//提供创建和修改图像的各种类。
import java.awt.Panel;//它是一个中间容器，是Container的子类，Applet的超类，用来存放awt组件
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;//点击，按下，松开，进入，离开
import java.awt.event.MouseMotionListener;//运动，离开
import java.awt.event.WindowAdapter;//接收窗口事件的抽象适配器类。此类中的方法为空。此类存在的目的是方便创建侦听器对象，WindowAdapter是抽象类，实现了所有的WindowListener方法，只不过方法内部都是空的。
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;//Image是一个抽象类，BufferedImage是其实现类，是一个带缓冲区图像类，主要作用是将一幅图片加载到内存中

public class JavaBilliards extends Panel implements Runnable, MouseListener,//extends继承类，implements实现接口
        MouseMotionListener {

	
    private static final long serialVersionUID = -5019885590391523441L;

    // 球
    private double _ball;

    // 缓存用背景
    private Image _screen;

    // 画布
    private Graphics _graphics;

    // 台桌
    private Image _table;
    private double xLeftBound;
    private double xRightBound;
    private double xInLeftBound;
    private double xInMiddle;
    private double xInRightBound;
    private double yTopBound;
    private double yBottomBound;
    private double yInTopBound;
    private double yInBottomBound;

    private int countBall;

    private int remainBallNum;

    private double xPosition[];//用来记录一个球静止时所处的位置

    private double yPosition[];

    private double xSpeed[];

    private double ySpeed[];

    private double ballCenterX[];//用来临时记录一个运动中的球的位置

    private double ballCenterY[];

    private double xSpeedAfterCrash[];

    private double ySpeedAfterCrash[];

    private boolean exist[];

    private double ballRound;

    private int state;

    private int beforeDraggedX;

    private int beforeDraggedY;

    private int nowMouseX;

    private int nowMouseY;

    private boolean mouseIsPressed;

    private int armLength;

    private int A;//该参数是为了让辅助线看上去“动起来”，是按时间增减的变量
	
    /**
     * 初始化
     * 
     */
    public JavaBilliards() {//构造方法
        state = 0;//游戏状态，0代表初始，1代表所有球均静止，2代表有球在运动，3代表所有球都已打完
        ballRound = 15d;//球的半径
        armLength = 300;//球杆长度相关
        A = 0;
        //setBounds(50, 50, 700, 350);
        Frame frame = new Frame("喜哥台球");
        frame.add(this);
        frame.setBounds(0, 0, 1400, 760);//前两个是窗口坐标，后两个窗口大小
        frame.setResizable(false);//屏幕是否可拉伸
        frame.setVisible(true);//窗口可见
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });//监听器，监听到窗口被关闭时退出程序
        requestFocus();//把输入焦点放在调用这个方法的控件上
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
        addMouseListener(this);//调用initialize方法时一定有一个对象，this就代表这个对象，此处代表JavaBilliards类
        addMouseMotionListener(this);
        // 启动
        new Thread(this).start();//thread类建立线程对象，start是该对象的方法，表示该线程开始执行
    }

    /**
     * 初始化数据
     * 
     */
    public void base() {

        // 球体
        _ball = 16D;//袋的半径
        xLeftBound=40D;//桌框外边界左边线X坐标
        xRightBound=getWidth()-40;//桌框外边界右边线X坐标
        yTopBound=xLeftBound;//桌框外边界上边线y坐标
        yBottomBound=(double) getHeight() - xLeftBound;//桌框外边界下边线y坐标
        xInLeftBound=xLeftBound + 20D;//桌框内边界左边线X坐标
        xInRightBound= xRightBound - 20D;//桌框内边界右边线X坐标
        xInMiddle=(xInLeftBound+ xInRightBound)/2;//桌框内边界中心x坐标
        yInTopBound=yTopBound + 20D;//桌框内边界上边线y坐标
        yInBottomBound=yBottomBound - 20D ;//桌框内边界下边线y坐标
    }

    /**
     * 注入实例
     * 
     */
    public void immit() {
        countBall = 16;//球的个数
        xPosition = new double[countBall];
        yPosition = new double[countBall];
        xSpeed = new double[countBall];
        ySpeed = new double[countBall];
        ballCenterX = new double[countBall];
        ballCenterY = new double[countBall];
        xSpeedAfterCrash = new double[countBall];
        ySpeedAfterCrash = new double[countBall];
        exist = new boolean[countBall];
        // 初始化打击对象
        hitObject();
        // 初始化打击用球
        hitBall();
    }

    //设置白球的状态和位置
    public void hitBall() {
        xPosition[0] = (1.0D * (xInRightBound - xInLeftBound)) / 3D;//球的X坐标
        yPosition[0] = this.getHeight() / 2;//球的纵坐标
        xSpeed[0] = 0.0D;//球的横向速度
        ySpeed[0] = 0.0D;//球的纵向速度
        exist[0] = true;//0代表母球，或许是该球是否还在场
    }
 
    //初始化设置被打的球
    public void hitObject() {
        int il = 1;
        remainBallNum = countBall - 1;//还剩几个球
        // 求平方根
        double dl = Math.sqrt(4D);
        for (int j1 = 0; j1 < 5; j1++) {
            double d2 = ((double) getWidth() * 2D) / 3D + (double) j1 * dl * ballRound;//球的横坐标等于宽度的2/3加球所处的排数乘半径乘根号3.5
            double d3 = (double) (getHeight() / 2) - (double) j1 * ballRound*1.2;//球的纵坐标等于高度的1/2减去球所处的排数乘半径
            for (int k1 = 0; k1 <= j1; k1++) {
                xPosition[il] = d2;
                yPosition[il] = d3;
                xSpeed[il] = 0.0D;
                ySpeed[il] = 0.0D;
                exist[il] = true;
                d3 += 2.2D * ballRound;//同一排的球后续每个的高度等于上一个的高度加2个半径
               
                il++;
            }

        }
        //exist[12]=true;

    }
    
    //画球桌
    public void makeTable() {
        _table = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        Graphics g = _table.getGraphics();
        g.setColor(Color.GRAY);//桌外背景色
        g.fillRect(0, 0, getWidth(), getHeight());//涂色矩形区域左上角坐标及宽高
        g.setColor((new Color(200, 100, 250)).darker());//设置球桌边框颜色
 
        g.fill3DRect((int) xLeftBound, (int) yTopBound, (int) (xRightBound - xLeftBound),
                (int) (yBottomBound - yTopBound),true);
        g.setColor(Color.GREEN.darker());//桌框内颜色
        g.fill3DRect((int) xInLeftBound, (int) yInTopBound, (int) (xInRightBound - xInLeftBound),
                (int) (yInBottomBound - yInTopBound), false);
        g.setColor(Color.GREEN.darker());//母球线颜色
        g.drawLine((int) ((1.0D * (xInRightBound - xInLeftBound)) / 3D), (int) yInTopBound,
                (int) ((1.0D * (xInRightBound - xInLeftBound)) / 3D), (int) yInBottomBound);
        g.fillOval((int) ((1.0D * (xInRightBound - xInLeftBound)) / 3D) - 2,
                (int) ((yInBottomBound + yInTopBound) / 2D) - 2, 4, 4);
        g.drawArc((int) ((1.0D * (xInRightBound - xInLeftBound)) / 3D) - 20,
                (int) ((yInBottomBound + yInTopBound) / 2D) - 20, 40, 40, 90, 180);
        g.setColor(Color.BLACK);
        double d1 = _ball - 2D;
        //下面画6个球袋
        g.fillOval((int) (xInLeftBound - d1), (int) (yInTopBound - d1),
                (int) (2D * d1), (int) (2D * d1));
        g.fillOval((int) (xInLeftBound - d1), (int) (yInBottomBound - d1),
                (int) (2D * d1), (int) (2D * d1));
        g.fillOval((int) (xInMiddle - d1), (int) (yInTopBound - d1),
                (int) (2D * d1), (int) (2D * d1));
        g.fillOval((int) (xInMiddle - d1), (int) (yInBottomBound - d1),
                (int) (2D * d1), (int) (2D * d1));
        g.fillOval((int) (xInRightBound - d1), (int) (yInTopBound - d1),
                (int) (2D * d1), (int) (2D * d1));
        g.fillOval((int) (xInRightBound - d1), (int) (yInBottomBound - d1),
                (int) (2D * d1), (int) (2D * d1));
       
    }

    /**
     * 线程处理
     */
    public void run() {//重写runable接口的run方法，处理各种state
        long timeStart;
        timeStart = System.currentTimeMillis();//获取系统时间
        // 死循环反复处理
        for (;;) {
            long timeEnd = System.currentTimeMillis();
            switch (state) {
            default:
                break;

            case 1://所有球均静止
                // 根据时间换算运动轨迹
                conversion(timeEnd - timeStart);
                // 过程处理
                course();
                break;

            case 2://有球在运动
                conversion(timeEnd - timeStart);
                // 过程处理
                course();
                boolean flag = true;
                for (int i1 = 0; flag && i1 < countBall; i1++)
                    flag = xSpeed[i1] == 0.0D && ySpeed[i1] == 0.0D;

                if (flag) {
                    state = 1;
                    // 击球
                    if (!exist[0]) {
                        hitBall();//重置白球
                    }
                }
                if (remainBallNum == 0)
                    state = 3;
                break;

            case 3://球全都被打进了
                hitObject();
                hitBall();
                state = 0;
                break;
            }

            repaint();//执行paint方法
            timeStart = timeEnd;
            try {
                Thread.sleep(10L);//线程休眠10ms
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
            if (exist[i1]) {
                xPosition[i1] = ballCenterX[i1];
                yPosition[i1] = ballCenterY[i1];//把球的位置存储到position里
            }
    }
    /**
     * 变换时间为动作数据
     * 
     * @param value
     */
    public void conversion(long value) {//正常运动无碰撞
        double d1 = (double) value / 1000D;
        for (int i1 = 0; i1 < countBall; i1++)
            if (exist[i1]) {
                ballCenterX[i1] = xPosition[i1] + xSpeed[i1] * d1;
                ballCenterY[i1] = yPosition[i1] + ySpeed[i1] * d1;
                xSpeed[i1] *= 0.99D;//速度按时间衰减速率
                ySpeed[i1] *= 0.99D;
                if (Math.abs(Math.hypot(xSpeed[i1], ySpeed[i1])) < 2D) {//速度小于2时强制停止
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            }

    }

    public void pocket() {//分别处理入6个袋的情况
        for (int i1 = 0; i1 < countBall; i1++)
            if (exist[i1]) {
            	if (Math.hypot(xInLeftBound - xPosition[i1], yInTopBound - yPosition[i1]) < _ball) {//球心与左上袋距离小于袋半径
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            	if (Math.hypot(xInLeftBound - xPosition[i1], yInBottomBound - yPosition[i1]) < _ball) {
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            	if (Math.hypot(xInMiddle - xPosition[i1], yInTopBound - yPosition[i1]) < _ball) {
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            	if (Math.hypot(xInMiddle - xPosition[i1], yInBottomBound - yPosition[i1]) < _ball) {
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            	if (Math.hypot(xInRightBound - xPosition[i1], yInTopBound - yPosition[i1]) < _ball) {
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }
            	if (Math.hypot(xInRightBound - xPosition[i1], yInBottomBound - yPosition[i1]) < _ball) {
                    exist[i1] = false;
                    if (i1 != 0) {
                        remainBallNum--;
                    }
                    xSpeed[i1] = 0.0D;
                    ySpeed[i1] = 0.0D;
                }

            }

    }

    public void play() {
        for (int i1 = 0; i1 < countBall; i1++)
            if (exist[i1]) {
                for (int j1 = i1 + 1; j1 < countBall; j1++) {
                    boolean flag;
                    if (exist[j1] && (flag = isCrashed(i1, j1))) {
                        for (int k1 = 0; k1 < 10 && flag; k1++) {
                            ballCenterX[i1] = (ballCenterX[i1] + xPosition[i1]) / 2D;
                            ballCenterY[i1] = (ballCenterY[i1] + yPosition[i1]) / 2D;
                            ballCenterX[j1] = (ballCenterX[j1] + xPosition[j1]) / 2D;
                            ballCenterY[j1] = (ballCenterY[j1] + yPosition[j1]) / 2D;
                            flag = isCrashed(i1, j1);
                        }//本次碰撞了，连续十次找与上次运动起点的二分点，查看是否碰撞。直到某次不碰为止。

                        if (flag) {//若1024分点依然碰，则认为上次运动的起点处就已经碰了。若某一点不碰了，则认为上一个碰的点处碰的。
                            ballCenterX[i1] = xPosition[i1];
                            ballCenterY[i1] = yPosition[i1];
                            ballCenterX[j1] = xPosition[j1];
                            ballCenterY[j1] = yPosition[j1];
                        }
                        double d1 = ballCenterX[j1] - ballCenterX[i1];//两球碰撞时X坐标差
                        double d2 = ballCenterY[j1] - ballCenterY[i1];
                        double d3 = Math.hypot(ballCenterX[i1] - ballCenterX[j1], ballCenterY[i1] - ballCenterY[j1]);//两球碰撞时距离
                        double d4 = d1 / d3;//X边比斜边
                        double d5 = d2 / d3;//Y比斜
                        xSpeedAfterCrash[j1] = xSpeed[j1] - xSpeed[j1] * d4 * d4;
                        xSpeedAfterCrash[j1] -= ySpeed[j1] * d4 * d5;
                        xSpeedAfterCrash[j1] += xSpeed[i1] * d4 * d4;
                        xSpeedAfterCrash[j1] += ySpeed[i1] * d4 * d5;
                        ySpeedAfterCrash[j1] = ySpeed[j1] - ySpeed[j1] * d5 * d5;
                        ySpeedAfterCrash[j1] -= xSpeed[j1] * d4 * d5;
                        ySpeedAfterCrash[j1] += xSpeed[i1] * d4 * d5;
                        ySpeedAfterCrash[j1] += ySpeed[i1] * d5 * d5;
                        xSpeedAfterCrash[i1] = xSpeed[i1] - xSpeed[i1] * d4 * d4;
                        xSpeedAfterCrash[i1] -= ySpeed[i1] * d4 * d5;
                        xSpeedAfterCrash[i1] += xSpeed[j1] * d4 * d4;
                        xSpeedAfterCrash[i1] += ySpeed[j1] * d4 * d5;
                        ySpeedAfterCrash[i1] = ySpeed[i1] - ySpeed[i1] * d5 * d5;
                        ySpeedAfterCrash[i1] -= xSpeed[i1] * d4 * d5;
                        ySpeedAfterCrash[i1] += xSpeed[j1] * d4 * d5;
                        ySpeedAfterCrash[i1] += ySpeed[j1] * d5 * d5;
                        xSpeed[i1] = xSpeedAfterCrash[i1];
                        ySpeed[i1] = ySpeedAfterCrash[i1];
                        xSpeed[j1] = xSpeedAfterCrash[j1];
                        ySpeed[j1] = ySpeedAfterCrash[j1];
                    }
                }

            }

    }

    public boolean isCrashed(int i1, int j1) {
        // 查看 两球是否碰撞
        return Math.hypot(ballCenterX[i1] - ballCenterX[j1], ballCenterY[i1] - ballCenterY[j1]) < 2D * ballRound;
    }

    /**
     * 限制区域
     * 
     */
    public void limit() {
        for (int i = 0; i < countBall; i++)
            if (exist[i]) {
                if (ballCenterX[i] - ballRound < xInLeftBound) {
                    ballCenterX[i] = xInLeftBound + ballRound;
                    xSpeed[i] *= -1D;
                } else if (ballCenterX[i] + ballRound > xInRightBound) {
                    ballCenterX[i] = xInRightBound - ballRound;
                    xSpeed[i] *= -1D;
                }
                if (ballCenterY[i] - ballRound < yInTopBound) {
                    ballCenterY[i] = yInTopBound + ballRound;
                    ySpeed[i] *= -1D;
                } else if (ballCenterY[i] + ballRound > yInBottomBound) {
                    ballCenterY[i] = yInBottomBound - ballRound;
                    ySpeed[i] *= -1D;
                }
            }

    }//碰桌全速反弹

    public void makeScreen(Graphics screenGraphics) {

        screenGraphics.drawImage(_table, 0, 0, null);
        if (exist[0]) {
            _graphics.setColor(Color.WHITE);//画还在场的母球（只是画）
            _graphics.fillOval((int) (xPosition[0] - ballRound), (int) (yPosition[0] - ballRound),
                    (int) (ballRound * 2D), (int) (ballRound * 2D));
        }
        
        screenGraphics.setColor(Color.RED);//画还在场的被打的球
        for (int i1 = 1; i1 < countBall; i1++)
            if (exist[i1])
            { screenGraphics.fillOval((int) (xPosition[i1] - ballRound), (int) (yPosition[i1] - ballRound),
                        (int) (ballRound * 2D), (int) (ballRound * 2D));
            screenGraphics.setColor(Color.BLACK);
            screenGraphics.setFont(new Font("黑体", Font.PLAIN, 16));
            screenGraphics.drawString(i1+"", (int)(xPosition[i1]-3),(int)(yPosition[i1])+3);
            screenGraphics.setColor(Color.RED);
            }
        
/*
        screenGraphics.setColor(Color.BLACK);//画所有球的轮廓线
        for (int j1 = 0; j1 < countBall; j1++)
            if (exist[j1]) {
                screenGraphics.drawOval((int) (xPosition[j1] - ballRound), (int) (yPosition[j1] - ballRound),
                        (int) (ballRound * 2D), (int) (ballRound * 2D));
            }
*/
        if (state == 1){
            makeHelper(screenGraphics);
        }
        if (state == 0) {
            int k1 = getWidth() / 2 - 85;
            int l1 = getHeight() / 2;
            screenGraphics.setColor(Color.BLACK);
            screenGraphics.drawString("点击画面开始", k1 + 2, l1 + 2);
           
                screenGraphics.setColor(Color.YELLOW);
     
            screenGraphics.drawString("点击画面开始", k1, l1);
        }//这里还整了个文字变换颜色显示
    }

    /**
     * 绘制球杆及辅助线
     * 
     * @param screenGraphics
     */
    public void makeHelper(Graphics screenGraphics) {
        double d1 = Math.hypot(xPosition[0] - (double) beforeDraggedX, yPosition[0] - (double) beforeDraggedY);//母球与拖拽前位置距离
        double d2 = ((double) beforeDraggedX - xPosition[0]) / d1;//X方向距离比斜边
        double d3 = ((double) beforeDraggedY - yPosition[0]) / d1;
        double d4 = mouseIsPressed ? n() / 10D : 1.0D;
        double d5 = xPosition[0] + d2 * (ballRound + d4);
        double d6 = xPosition[0] + d2 * (ballRound + (double) armLength + d4);
        double d7 = yPosition[0] + d3 * (ballRound + d4);
        double d8 = yPosition[0] + d3 * (ballRound + (double) armLength + d4);
        screenGraphics.setColor(Color.ORANGE.darker());
        Graphics2D g2 = (Graphics2D)screenGraphics;
        g2.setStroke(new BasicStroke(6.0f));//粗细
        g2.drawLine((int) d5, (int) d7, (int) d6, (int) d8);//画球杆
        g2.setStroke(new BasicStroke(0.5f));
        int i1 = 0;
        int j1 = mouseIsPressed ? (int) (150D * (d4 / 1000D)) : 15;
        double d9=30D;//辅助线上两球的距离
        double d10 = d9 * d2;
        double d11 = d9 * d3;
        double d12 = xPosition[0] + (double) A * d2;//辅助线上球心横坐标
        double d13 = yPosition[0] + (double) A * d3;//辅助线上球心纵坐标
        A++;
        A %= d9;//这里改变A的值，实际上是让辅助线看上去动起来。随时间改变每个球的球心
        screenGraphics.setColor(Color.WHITE);
        for (; i1 < j1; i1++) {
            if (d12 < xInLeftBound) {
                d12 = xInLeftBound - d12;
                d12 = xInLeftBound + d12;
                d10 *= -1D;
            } else if (d12 > xInRightBound) {
                d12 -= xInRightBound;
                d12 = xInRightBound - d12;
                d10 *= -1D;
            }
            if (d13 < yInTopBound) {
                d13 = yInTopBound - d13;
                d13 = yInTopBound + d13;
                d11 *= -1D;
            } else if (d13 > yInBottomBound) {
                d13 -= yInBottomBound;
                d13 = yInBottomBound - d13;
                d11 *= -1D;
            }
            screenGraphics.fillOval((int) d12 - 2, (int) d13 - 2, 4,4);//画辅助线，辅助线每个点实际上是个小圆
            d12 -= d10;
            d13 -= d11;
        }

    }

    public double n() {
        if (mouseIsPressed) {
            return Math.min(1000D, 10D * Math.hypot(xPosition[0] - (double) nowMouseX, yPosition[0]
                    - (double) nowMouseY));
        } else {
            return Math.min(1000D, 10D * Math.hypot(beforeDraggedX - nowMouseX, beforeDraggedY - nowMouseY));
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        makeScreen(_graphics);
        g.drawImage(_screen, 0, 0, null);
    }

    public void mousePressed(MouseEvent mouseevent) {//按住不松开
        mouseIsPressed = true;
    }

    public void mouseReleased(MouseEvent mouseevent) {//鼠标点击包括了鼠标释放，但鼠标拖拽松开也会触发鼠标释放
        if (state == 1) {
            double d1 = Math.hypot(xPosition[0] - (double) beforeDraggedX, yPosition[0] - (double) beforeDraggedY);//母球到鼠标的距离
            double d2 = (xPosition[0] - (double) beforeDraggedX) / d1;//母球与鼠标位置横坐标差
            double d3 = (yPosition[0] - (double) beforeDraggedY) / d1;
            double d4;
            if ((d4 = n()) > 0.0D) {
                state = 2;
                xSpeed[0] = d4 * d2*3;
                ySpeed[0] = d4 * d3*3;
            }
        }
        mouseIsPressed = false;
    }

    public void mouseClicked(MouseEvent mouseevent) {
        if (state == 0)
            state = 1;
    }//鼠标点击

    public void mouseEntered(MouseEvent mouseevent) {
   
    }//鼠标进入窗口

    public void mouseExited(MouseEvent mouseevent) {

    }//鼠标离开窗口

    public void mouseMoved(MouseEvent mouseevent) {
        nowMouseX = mouseevent.getX();
        nowMouseY = mouseevent.getY();
        beforeDraggedX = nowMouseX;
        beforeDraggedY = nowMouseY;    
    }

    public void mouseDragged(MouseEvent mouseevent) {//鼠标拖动
        nowMouseX = mouseevent.getX();
        nowMouseY = mouseevent.getY();       
    }

    public static void main(String args[]) {
        new JavaBilliards();
    }

}
