package com.sotk.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;

public class Mushroom extends Creature{
	Level level;
	Animation idle;
	
	public Mushroom(int x, int y, Level level) {
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		this.level = level;
		
		loadAnimations();
	}

	private void loadAnimations() {
		// TODO Auto-generated method stub
		idle = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Idle.png"), 0, 150, 150, 4, 0.1f);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeHit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

}
