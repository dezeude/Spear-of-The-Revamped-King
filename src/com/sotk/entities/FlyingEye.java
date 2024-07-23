package com.sotk.entities;

import java.awt.Graphics;

import com.sotk.managers.AssetsManager;

public class FlyingEye extends Enemy{
	
	public FlyingEye(int x, int y) {
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/flying_eye/attack.png");
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		
	}

	@Override
	public void doPhysics() {
		
	}

}
