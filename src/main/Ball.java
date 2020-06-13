package main;

import java.awt.Color;

public class Ball {

	
	public  Ball(double radius) {
		this.radius=radius;
	}
	
	public  Ball(double xPosition,double yPosition,double radius) {
		this.xPosition=xPosition;
		this.yPosition=yPosition;
		this.radius=radius;
	}
	
	private double xPosition;
	
	private double yPosition;
	
	private double xSpeed;
	
	private double ySpeed;
	
	private double ballCenterX;
	
	private double ballCenterY;
	
	private double xSpeedAfterCrash;
	
	private double ySpeedAfterCrash;
	
	private boolean exist;
	
	private double radius;
	
	private Color color;

	public double getxPosition() {
		return xPosition;
	}

	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	public double getyPosition() {
		return yPosition;
	}

	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}

	public double getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public double getySpeed() {
		return ySpeed;
	}

	public void setySpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	public double getBallCenterX() {
		return ballCenterX;
	}

	public void setBallCenterX(double ballCenterX) {
		this.ballCenterX = ballCenterX;
	}

	public double getBallCenterY() {
		return ballCenterY;
	}

	public void setBallCenterY(double ballCenterY) {
		this.ballCenterY = ballCenterY;
	}

	public double getxSpeedAfterCrash() {
		return xSpeedAfterCrash;
	}

	public void setxSpeedAfterCrash(double xSpeedAfterCrash) {
		this.xSpeedAfterCrash = xSpeedAfterCrash;
	}

	public double getySpeedAfterCrash() {
		return ySpeedAfterCrash;
	}

	public void setySpeedAfterCrash(double ySpeedAfterCrash) {
		this.ySpeedAfterCrash = ySpeedAfterCrash;
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Color getColor() {
	    return color;
	}

	public void setColor(Color color) {
	    this.color = color;
	}
	

	
	
}
