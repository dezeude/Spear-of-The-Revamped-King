package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Bound;
import com.sotk.managers.Camera;
import com.sotk.managers.Collisions;
import com.sotk.states.creaturestates.AttackingState;

public class Goblin extends Enemy {
	// position offsets
	// render bounds
//	private int renderWidth = 150, renderHeight = 150;
	// sprite sheet
	static BufferedImage sheet = null;

	public Goblin(int x, int y) {
		xOff = 64; yOff = 64;
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/goblin/goblinSprite.png");
		loadAnimations();
		curAnim = idle;
	}

	private void loadAnimations() {
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		run = new Animation(sheet, 1, 150, 150, 8, 0.2f);
		takeHit = new Animation(sheet, 5, 150, 150, 4, 0.14f);
		death = new Animation(sheet, 6, 150, 150, 4, 0.14f);
		attack = new Animation(sheet, 2, 150, 150, 8, 0.14f);
		attack.setAttackFrame(6, 80, 71, 40, 30);
	}
	
	@Override
	public void update() {
		if (alive) {
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
			} else if (!Level.curLevel.getPlayer().isAlive())
				velocity.x = 0;

		}

		super.update();
	}

}
