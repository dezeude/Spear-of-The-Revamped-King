package com.sotk.managers;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class Animation {
	private float index;
	private float incrementer;
	BufferedImage[]frames;
	BufferedImage[]mirrorFrames;
	Rectangle[]attackFrames;
	private boolean locked = false;
	public Animation(Animation a) {
//this constructor makes this animation share the image objects with other animations to save memory
		this.incrementer = a.incrementer;
		this.frames = a.getFrames();
		this.mirrorFrames = a.getMirrorFrames();
		this.attackFrames = new Rectangle[frames.length];
		for(int i = 0; i < frames.length; i++) {
			attackFrames[i] = null;
		}
	}
	public Animation(String path, int length, float incrementer) {
		this.incrementer = incrementer;
		this.frames = new BufferedImage[length];
		this.mirrorFrames = new BufferedImage[length];
		this.attackFrames = new Rectangle[frames.length];
		
		BufferedImage img = AssetsManager.loadImage(path);
		for(int i = 0; i < length; i++) {
			frames[i] = img.getSubimage(i * 150, 0, 150, 150);;
		}
		
		for(int i = 0; i < length; i++) {
			mirrorFrames[i] = AssetsManager.getMirrorImage(frames[i]);
		}
		
		for(int i = 0; i < length; i++) {
			attackFrames[i] = null;
		}
		
		
		
	}
	
	public Animation(BufferedImage sheet, int yPos, int width, int height, int length, float incrementer) {
		this.incrementer = incrementer;
		this.frames = new BufferedImage[length];
		this.mirrorFrames = new BufferedImage[length];
		this.attackFrames = new Rectangle[frames.length];
		
		for(int i = 0; i < length; i++) {
			frames[i] = sheet.getSubimage(i * width, yPos * height, width, height);
		}
		
		for(int i = 0; i < length; i++) {
			mirrorFrames[i] = AssetsManager.getMirrorImage(frames[i]);
		}
		
		for(int i = 0; i < length; i++) {
			attackFrames[i] = null;
		}
		
		for(int i = 0; i < length; i++) {
			attackFrames[i] = null;
		}
		
		
	}
	
	public void play() {
		if(!locked)
			index += incrementer;
			
		
//		System.out.println("cast: " + (int)index);
//		System.out.println("round: " + Math.round(index));
	}
	
	public BufferedImage getCurFrame() {
		return frames[getIndex()];
	}
	
	public BufferedImage[] getFrames() {
		return this.frames;
	}
	
	public BufferedImage[] getMirrorFrames() {
		return this.mirrorFrames;
	}
	
	public BufferedImage getMirrorFrame() {
		return mirrorFrames[getIndex()];
	}
	
	public void setAttackFrame(int attackIndex, int xOff, int yOff, int width, int height) {
// the x and y offsets are based offsets from the top-left corner of the frame/render x and y
		attackFrames[attackIndex] = new Rectangle(xOff, yOff, width, height);
	}
	
	public void setAttackFrame(int xOff, int yOff, int width, int height) {
// the x and y offsets are based offsets from the top-left corner of the frame/render x and y
		for(int i = 0; i < attackFrames.length; i++)
			attackFrames[i] = new Rectangle(xOff, yOff, width, height);
	}
	
	public void setAttackFrame(int attackIndex, Rectangle bounds) {
		attackFrames[attackIndex] = bounds;
	}
	
	public void setAttackFrame(Rectangle bounds) {
		for(int i = 0; i < attackFrames.length; i++)
			attackFrames[i] = bounds;
	}
	
	public Rectangle getAttackFrame() {
		if(attackFrames == null || attackFrames[getIndex()] == null)
			return null;
		return attackFrames[getIndex()];
	}
	
	public int getIndex() {
		return (int) (Math.floor(index) % frames.length);
	}
	
	public boolean hasAttackFrame() {
		if(attackFrames == null)
			return false;
		if(attackFrames[getIndex()] != null)
			return true;
		return false;
	}
	
	public void reset() {
		index = 0;
	}
	
	public int length() {
		return frames.length;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
	public boolean isFinished() {return index >= frames.length - 1;}
	
	
}
