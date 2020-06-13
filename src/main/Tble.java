package main;

public class Tble {

	public Tble(double xLeftPosition,double yTopPosition,double width, double height) {
		this.xLeftPosition = xLeftPosition;
		this.yTopPosition = yTopPosition;
		this.width = width;
		this.height = height;
		this.xRightPosition=xLeftPosition+width;
		this.yDownPosition=yTopPosition+height;
		
	}
	private double xLeftPosition;
	
	private double yTopPosition;
	
	private double xRightPosition;
	
	private double yDownPosition;
	
	private double width;
	
	private double height;

	public double getxLeftPosition() {
		return xLeftPosition;
	}

	public void setxLeftPosition(double xLeftPosition) {
		this.xLeftPosition = xLeftPosition;
	}

	public double getyTopPosition() {
		return yTopPosition;
	}

	public void setyTopPosition(double yTopPosition) {
		this.yTopPosition = yTopPosition;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getxRightPosition() {
		return xRightPosition;
	}

	public void setxRightPosition(double xRightPosition) {
		this.xRightPosition = xRightPosition;
	}

	public double getyDownPosition() {
		return yDownPosition;
	}

	public void setyDownPosition(double yDownPosition) {
		this.yDownPosition = yDownPosition;
	}
	
}
