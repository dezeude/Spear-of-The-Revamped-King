package com.sotk.managers;

import com.sotk.main.GamePanel;

public class Camera {
//	private static Camera instance = new Camera();
	
	private static int xOffset = 0, yOffset = 0, divisor = 1;
// The x and y offsets just represent the position of the top-left corner of the camera.
	
//	public static Camera getInstance() {
//		return instance;
//	}
	
	public static void setDivisor(int div) {
//the divisor makes the camera slide towards it's destination
//(the player) slower the higher it is.
		divisor = div;
	}
	public static void smoothTo(int x, int y) {//TODO: FIX THIS!
// Smoothly moves the camera so that the center is the argument position (x,y) by changing the x and y 
// offsets by the difference of the position and dividing it by the smoothing divisor number.

		xOffset += (x - xOffset - (GamePanel.getGraphicsWidth() / 2))	/ divisor;
		yOffset += (y - yOffset - (GamePanel.getGraphicsHeight() * 0.7))	/ divisor;
		
		//Makes the argument position eventually appear at the middle of the screen width,
		//and 7/10 of the screen height.
		
		if(xOffset < 0) 
			xOffset = 0;
		if (yOffset < 0)
			yOffset = 0;			
		if(xOffset > TileMap.width * TileMap.TILELENGTH)
			xOffset = TileMap.width * TileMap.TILELENGTH;
		if(yOffset > TileMap.height * TileMap.TILELENGTH)
			yOffset = TileMap.height * TileMap.TILELENGTH;
	}
	public static void setPos(int x, int y) {
		xOffset = x;
		yOffset = y;
	}
	public static int getXOffset() {
		return xOffset;
	}
	public static int getYOffset() {
		return yOffset;
	}
}
