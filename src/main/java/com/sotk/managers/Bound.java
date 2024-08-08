package com.sotk.managers;

import java.awt.Graphics;

public class Bound {
	private int x,y, width, height;
	private boolean top = false, bottom = false, left = false, right = false;
	
	public Bound(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public Bound(int x, int y, boolean top, boolean bottom, boolean left, boolean right) {
		this.x = x;
		this.y = y;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isTop() {
		return top;
	}
	
	public void setTop(boolean top) {
		this.top = top;
	}
	
	public boolean isBottom() {
		return bottom;
	}
	
	public void setBottom(boolean bottom) {
		this.bottom = bottom;
	}
	
	public boolean isLeft() {
		return left;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public boolean isRight() {
		return right;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics g) {
		g.fillRect(x, y, width, height);
	}
}
