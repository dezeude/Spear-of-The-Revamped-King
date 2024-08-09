package com.sotk.entities;

import java.awt.image.BufferedImage;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;

public class King extends NPC{
	//sprite sheet
	private static BufferedImage sheet;
	//animations
	Animation idle;
	Animation run;
	Animation takeHit;
	Animation death;
	Animation attack;
	BufferedImage curFrame;
	
	public King(int x, int y, Level level) {
		super(x, y, level);
		
		bw = 15;
		bh = 46;
		
		//position offsets
		xOff = 80;
		yOff = 60;
		
		//render bounds
		renderWidth = 160;
		renderHeight = 111;
		String[] temp = {"Hello Young Warrior!"};
		setSpeech(temp);
		
		loadAnimations();
		curAnim = idle;
	}
	
	private void loadAnimations() {
		idle = new Animation(
				AssetsManager.loadImage("/sprites/mobs/NPCs/King/Idle.png"),
				0, 160, 111, 8, 0.1f);
	}
	
	public void update() {
		if(level.getPlayer().getBounds().x > position.x)
			facingRight = true;
		else
			facingRight = false;
		curAnim.play();
		
		checkSpeech();
	}
	
}
