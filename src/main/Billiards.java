package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Billiards extends Panel {

	private static final int ballNum = 16;

	private static final double radius = 20D;

	private Ball[] balls = new Ball[ballNum];

	private static final double OutBound = 80D;
	private static final double Bound = 40D;
	private static final double InBound = OutBound + Bound;
	private Color ballColor[] = { Color.WHITE, Color.YELLOW, Color.BLUE, Color.RED, Color.MAGENTA, Color.ORANGE,
			Color.GREEN, Color.RED.darker(), Color.BLACK, Color.YELLOW, Color.BLUE, Color.RED, Color.MAGENTA,
			Color.ORANGE, Color.GREEN, Color.RED.darker() };
	private Tble outTble;
	private Tble inTble;

	public void excute() {
		Frame frame = new Frame("台球");

		frame.add(this);
		frame.setBounds(0, 0, 1400, 800);// 前两个是窗口坐标，后两个窗口大小
		frame.setResizable(false);// 屏幕是否可拉伸
		frame.setVisible(true);// 窗口可见
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});// 监听器，监听到窗口被关闭时退出程序
		requestFocus();// 把输入焦点放在调用这个方法的控件上
		initialize(frame);
	}

	public void initialize(Frame frame) {

		outTble = new Tble(frame.getX() + OutBound, frame.getY() + OutBound, frame.getWidth() - 2D * OutBound,
				frame.getHeight() - 2D * OutBound);
		inTble = new Tble(frame.getX() + InBound, frame.getY() + InBound, frame.getWidth() - 2D * InBound,
				frame.getHeight() - 2D * InBound);
		balls[0] = setWhiteBall(inTble);
		sethitObjectBalls(inTble);

		// repaint();
	}

	private Ball setWhiteBall(Tble table) {
		Ball ball = new Ball(radius);
		ball.setxPosition(table.getxLeftPosition() + (table.getWidth() / 3D));
		ball.setyPosition(table.getyTopPosition() + (table.getHeight()) / 2D);
		ball.setxSpeed(0.0D);
		ball.setySpeed(0.0D);
		ball.setColor(ballColor[0]);
		ball.setExist(true);
		return ball;
	}

	private void sethitObjectBalls(Tble table) {
		double ballxspace = Math.sqrt(3D) * radius;

		for (int j = 0, i = 1; j < 5; j++) {

			double xPosition = (double) (table.getxLeftPosition() + (table.getWidth() * 2D / 3D))
					+ (double) j * ballxspace;
			double yPosition = (double) (table.getyTopPosition() + (table.getHeight() / 2D)) - (double) j * radius;
			for (int k = 0; k <= j; k++) {
				balls[i] = new Ball(radius);
				balls[i].setxPosition(xPosition);
				balls[i].setyPosition(yPosition);
				balls[i].setxSpeed(0.0D);
				balls[i].setySpeed(0.0D);
				balls[i].setExist(true);
				balls[i].setColor(ballColor[i]);
				yPosition += 2.0D * radius;
				i++;
			}

		}
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 1400, 800);
		paintTable(g);
		paintBalls(g);
	}

	public void paintTable(Graphics g) {
		g.setColor(Color.GRAY);// 桌外背景色
		g.fillRect(0, 0, getWidth(), getHeight());// 涂色矩形区域左上角坐标及宽高

		g.setColor((new Color(200, 100, 250)).darker());// 设置球桌边框颜色
		g.fill3DRect((int) outTble.getxLeftPosition(), (int) outTble.getyTopPosition(), (int) outTble.getWidth(),
				(int) outTble.getHeight(), true);
//	 

		g.setColor(Color.GREEN.darker());// 桌框内颜色
		g.fill3DRect((int) inTble.getxLeftPosition(), (int) inTble.getyTopPosition(), (int) inTble.getWidth(),
				(int) inTble.getHeight(), false);
//	 
		g.setColor(Color.GREEN.darker());// 母球线颜色
		g.drawLine((int) (inTble.getxLeftPosition() + (inTble.getWidth() / 3D)), (int) InBound,
				(int) (inTble.getxLeftPosition() + (inTble.getWidth() / 3D)),
				(int) (inTble.getyTopPosition() + inTble.getHeight()));

		g.fillOval((int) ((inTble.getxLeftPosition() + (inTble.getWidth() / 3D)) - radius / 4D),
				(int) ((inTble.getHeight() / 2D + inTble.getyTopPosition()) - radius / 4D), (int) (radius / 2D),
				(int) (radius / 2D));
		g.drawArc((int) ((inTble.getxLeftPosition() + (inTble.getWidth() / 3D)) - (2D * radius)),
				(int) ((inTble.getHeight() / 2D + inTble.getyTopPosition()) - (2D * radius)), (int) (4D * radius),
				(int) (4D * radius), 90, 180);

		g.setColor(Color.BLACK);
		double d = radius * 4D / 5D;
		g.fillOval((int) (inTble.getxLeftPosition() - d), (int) (inTble.getyTopPosition() - d), (int) (2.5D * d),
				(int) (2.5D * d));
		g.fillOval((int) (inTble.getxLeftPosition() - d), (int) (inTble.getyDownPosition() - d), (int) (2.5D * d),
				(int) (2.5D * d));
		g.fillOval((int) (inTble.getxLeftPosition() + inTble.getWidth() / 2D - d), (int) (inTble.getyTopPosition() - d),
				(int) (2.5D * d), (int) (2.5D * d));
		g.fillOval((int) (inTble.getxLeftPosition() + inTble.getWidth() / 2D - d),
				(int) (inTble.getyDownPosition() - d), (int) (2.5D * d), (int) (2.5D * d));
		g.fillOval((int) (inTble.getxRightPosition() - d), (int) (inTble.getyTopPosition() - d), (int) (2.5D * d),
				(int) (2.5D * d));
		g.fillOval((int) (inTble.getxRightPosition() - d), (int) (inTble.getyDownPosition() - d), (int) (2.5D * d),
				(int) (2.5D * d));

	}

	public void paintBalls(Graphics g) {
		for (int i = 0; i < balls.length; i++) {
			if (balls[i].isExist()) {
				g.setColor(balls[i].getColor());
				System.out.println(i);
				g.fillOval((int) (balls[i].getxPosition() - radius), (int) (balls[i].getyPosition() - radius),
						(int) (2D * radius), (int) (2D * radius));
			}

		}
	}

	public static void main(String[] args) {
		new Billiards().excute();
	}
}
