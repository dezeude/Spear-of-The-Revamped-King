package com.sotk.managers;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Collisions {
	//handle all collisions in the game
	//just like Terraria
	
	private static int[][] map; //collisions map
	private int tileLength = TileMap.TILELENGTH; // tile length
	
	public static void setMap(int[][] collisionMap) {
		map = collisionMap;
	}
	
	public static Bound worldCollision(int bx, int by, int bw, int bh, int vx, int vy){
		boolean top = false, bottom = false, left = false, right = false;
		//get all the possible tiles in a list
		int TILELENGTH = TileMap.TILELENGTH;
		ArrayList<Rectangle> hitList = new ArrayList();
		for(int i = by / TILELENGTH - 2; i < (by + bh) / TILELENGTH + 2; i++) {
			for(int j = bx / TILELENGTH - 2; j < (bx + bw) / TILELENGTH + 2; j++) {
				if(i >= 0 && i < map.length && j >= 0 && j < map[i].length && map[i][j] - 1 > -1)
					hitList.add(new Rectangle(j*TILELENGTH,i*TILELENGTH,TILELENGTH,TILELENGTH));
			}
		}

		//calculate collisions on the x-axis first
		bx += vx;
		Rectangle newBounds = new Rectangle(bx, by, bw, bh);
		for(Rectangle r: hitList) {
			if(newBounds.intersects(r)) {
//				System.out.println("collision found on x-axis");
				if(vx > 0) { //if the tile is on the right of the player
					bx = r.x-newBounds.width;
					right = true;
//					velx = 0;
				}
				if(vx < 0) {//if the tile is on the left of the player
					bx = r.x+r.width;
					left = true;
//					velx = 0;
				}
			}			
		}
					
				
				//calculate collisions on the y-axis second
		by += vy;
		newBounds = new Rectangle(bx, by, bw, bh);
		for(Rectangle r: hitList) {
			if(newBounds.intersects(r)) {
//				System.out.println("collision found on y-axis");
				if(vy > 0) { //if the tile is under the player
					by = r.y-newBounds.height;
					bottom = true;
				}
				if(vy < 0) {//if the tile is above the player
					by = r.y+r.height;
					top = true;
				}
			}
		}
		
		
		return new Bound(bx,by,top,bottom,left,right);
	}  
}
