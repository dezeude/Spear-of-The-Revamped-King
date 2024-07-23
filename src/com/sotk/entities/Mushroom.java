package com.sotk.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Camera;

public class Mushroom extends Enemy {
	static BufferedImage sheet = null;
	private int xOff = 64, yOff = 64;

	public Mushroom(int x, int y) {
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Idle.png");
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		curAnim = idle;
	}

	@Override
	public void update() {
		curAnim.play();
		curFrame = curAnim.getCurFrame();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(curFrame, position.x - xOff - Camera.getXOffset(), position.y - yOff - Camera.getYOffset(), null);
	}

	@Override
	public void doPhysics() {

	}

}
