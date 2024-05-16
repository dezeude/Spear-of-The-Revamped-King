package com.sotk.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.levels.Level;
import com.sotk.main.GamePanel;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Camera;
import com.sotk.managers.TileMap;

public abstract class NPC extends Entity implements Interactable{
	protected boolean top = false, bottom = false, left = false, right = false;
	protected int bw, bh, health;
	private int TILELENGTH = TileMap.TILELENGTH;
	protected float velx,vely;
	protected float gravity = 0.45f;
	protected boolean facingRight = true, inAnimation = false;
	protected Level level;
	protected double yOffset = 0;
	
	protected int xOff = 0, yOff = 0;
	protected int renderWidth = 0, renderHeight = 0;
	
	protected Animation curAnim;
	
	protected Animation[] animations; //maybe??
	
	protected String speech[] = null;
	protected int speechIndex;
	protected int speechRange = 100;
	protected boolean speechReady = false;
	
	protected static BufferedImage selectImage; 
	protected int selectX, selectY;
//arrow select image that hovers above the selected NPC with speech.
	
	static int dialogueWidth = (int)(GamePanel.getGraphicsWidth() * 0.5);
	static int dialogueHeight = (int)(GamePanel.getGraphicsHeight() * 0.35);
	static int dialogueX = (int)(GamePanel.getGraphicsWidth() / 2) - dialogueWidth / 2;
	static int dialogueY = (int)(GamePanel.getGraphicsHeight() * 0.025);
	static int dialogueArc = 50;
	static int dialogueOffset = 30;
	static float tick = 0;
	
	static Color dialogueColor = new Color(1f,
			228 / 255f,
			205 / 255f,
			0.8f);
	static Font dialogueFont = new Font(Font.SERIF, Font.PLAIN, 20);
	
	protected boolean dialogueVisible = false;
	
	public NPC(int x, int y, Level level) {
		position.x = x;
		position.y = y;
		this.level = level;
		speechIndex = -1;
		selectImage = AssetsManager.loadImage("/worlds/Assets/arrow.png");
	}

	@Override
	public abstract void update();
	
	protected void checkSpeech() {
		if(speech != null && //if a dialogue is available
			level.distFromPlayer(getBounds()) < speechRange) {
			//if the player is close enough to talk to the NPC
			speechReady = true;
			level.selectEntity(this);
		}
			
		else {
			speechReady = false;
			dialogueVisible = false;
		}
			
	}

	@Override
	public void render(Graphics g) {
		if(facingRight)
			g.drawImage(curAnim.getCurFrame(), position.x - xOff - Camera.getXOffset(), position.y - yOff - Camera.getYOffset(), renderWidth, renderHeight, null);
		else
			g.drawImage(curAnim.getMirrorFrame(), position.x - xOff - Camera.getXOffset(), position.y - yOff - Camera.getYOffset(), renderWidth, renderHeight, null);
		
		if(speechReady) {
			//draw the selectImage
			selectX =  (((position.x * 2 + bw) / 2) - 24 - Camera.getXOffset());
			selectY =  (position.y - bh - Camera.getYOffset());
			yOffset = Math.sin(tick);
			tick += 0.05f;
			g.drawImage(selectImage, selectX,(int)(yOffset * 10) - 4 + selectY, null);
		}
		
		if(dialogueVisible) {
			dialogueX = (int)(GamePanel.getGraphicsWidth() / 2) - dialogueWidth / 2;
			dialogueY = (int)(GamePanel.getGraphicsHeight() * 0.025);
			
			g.setColor(dialogueColor);
			g.fillRoundRect(dialogueX,
							dialogueY,
							dialogueWidth,
							dialogueHeight,
							dialogueArc,
							dialogueArc);
			g.setColor(Color.black);
			g.setFont(dialogueFont);
			
			for(String line: speech[speechIndex].split("@")) {
				g.drawString(line, dialogueX + dialogueOffset, dialogueY + dialogueOffset);
				dialogueY += 40;
			}
			
			
		}
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return position.x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return position.y;
	}
	
	public Vector2i getPos() {return position;}
	
	public void move(int vx, int vy) {
		int bx = position.x;
		int by = position.y;
		//get all the possible tiles in a list
		ArrayList<Rectangle> hitList = new ArrayList();
		for(int i = by / TILELENGTH - 2; i < (by + bh) / TILELENGTH + 2; i++) {
			for(int j = bx / TILELENGTH - 2; j < (bx + bw) / TILELENGTH + 2; j++) {
				if(i >= 0 && i < TileMap.map.length && j >= 0 && j < TileMap.map[i].length && TileMap.map[i][j] - 1 > -1)
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
					velx = 0;
				}
				if(vx < 0) {//if the tile is on the left of the player
					bx = r.x+r.width;
					left = true;
					velx = 0;
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
					vely = 0;
				}
			}
		}
		
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle(position.x,position.y,bw,bh);
	}
	
	public void setSpeech(String[] speech) {
		this.speech = speech;
	}
	
	public void speak() {
		dialogueVisible = true;
		if(speechIndex + 1 < speech.length) {
			speechIndex++;
		}
		else {
			dialogueVisible = false;
			speechIndex = -1;
		}
			
	}
	
	public void interact() {
		// TODO Auto-generated method stub
		if(level.distFromPlayer(getBounds()) <= speechRange)
			speak();
	}
	
	@Override
	public void addExtras(String[] extras) {
		speech = extras;
	}
}
