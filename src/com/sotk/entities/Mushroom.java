package com.sotk.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Camera;

public class Mushroom extends Enemy {
	static BufferedImage sheet = null;

	public Mushroom(int x, int y) {
		xOff = 64; yOff = 64;
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Idle.png");
		loadAnimations();
		curAnim = idle;
	}
	
	private void loadAnimations() {
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		run = new Animation(
				AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Run.png"),
				0,150,150,8, 0.2f);
		
	}

	@Override
	public void update() {
		if (Level.curLevel.getPlayer().alive && Level.curLevel.getPlayer().getDist(getBounds()) <= 250) {
			// if the player is alive
			// move towards the player
			if (Level.curLevel.getPlayer().getDist(getBounds()) <= 20) {
				// the player is right in front of the goblin
				velocity.x = 0;
			} else if ((Level.curLevel.getPlayerBounds().x + Level.curLevel.getPlayerBounds().width)
					/ 2 > (this.position.x + this.bw) / 2) {
				// the player is on the right
				velocity.x = 2;
				facingRight = true;
			}

			else if ((Level.curLevel.getPlayerBounds().x + Level.curLevel.getPlayerBounds().width)
					/ 2 < (this.position.x + this.bw) / 2) {
				// the player is on the left
				velocity.x = -2;
				facingRight = false;
			}
		}
		
		super.update();
	}

}
