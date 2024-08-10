package com.sotk.levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sotk.main.GamePanel;
import com.sotk.managers.AssetsManager;

public class BackImage {
	private BufferedImage img;
	private float x, x2, speed;
	private int xOff, yOff;
	/**
	 * Constructs an object with two image positions that draw the images.
	 */
	public BackImage(String path, float speed) {
		this.x = 0; //first image
		this.x2 = GamePanel.getGraphicsWidth(); //second image
		this.speed = speed;
		img = AssetsManager.loadImage(path);
	}
	
	public BackImage(String path, float speed, int xOff, int yOff) {
		this.x = 0;
		this.x2 = GamePanel.getGraphicsWidth();
		this.speed = speed;
		img = AssetsManager.loadImage(path);
		this.xOff = xOff;
		this.yOff = yOff;
	}
	
	public void crop(int xOff, int yOff, int widthOff, int heightOff) {
		img = img.getSubimage(xOff, yOff, widthOff, heightOff);
	}
	
	public void moveLeft() {//moves the background to the right, but gives the feeling of the scene moving left.
		if((int)x == 0)
			x2 = -GamePanel.getGraphicsWidth();
		if((int)x2 == 0)
			x = -GamePanel.getGraphicsWidth();
		x += speed;
		x2 += speed;
	}
	
	public void moveRight() { //moves the background to the left, but gives the feeling of the scene moving right.
		if((int)x == 0)
			x2 = GamePanel.getGraphicsWidth();
		if((int)x2 == 0)
			x = GamePanel.getGraphicsWidth();
		x -= speed;
		x2 -= speed;
	}
	
	public void render(Graphics g) {
		//first image
		g.drawImage(img, (int)x + xOff,yOff, null);
		//+1 width to fix background gap
		//second image
		g.drawImage(img, (int)x2, yOff, null);
	}
}
