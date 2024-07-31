package com.sotk.entities;

import java.awt.image.BufferedImage;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;

import org.joml.Vector2f;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.states.creaturestates.CreatureState;
import com.sotk.states.creaturestates.IdleState;

public class Mushroom extends Enemy {
	static BufferedImage sheet = null;
	public Animation throwAttack;
	public boolean pursuing = false;

	public Mushroom(int x, int y) {
		xOff = 64;
		yOff = 64;
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		attackRange = bw * 15;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Idle.png");
		loadAnimations();
		curAnim = idle;
		this.state = new IdleState();
	}

	private void loadAnimations() {
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		run = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Run.png"), 0, 150, 150, 8, 0.2f);
		attack = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Attack.png"), 0, 150, 150, 8,
				0.15f);
		takeHit = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Take_Hit.png"), 0, 150, 150,
				4, 0.15f);
		death = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Death.png"), 0, 150, 150, 4,
				0.15f);

	}

	@Override
	public void update() {
		super.update();
		if (alive) {
			if (Level.curLevel.getPlayer().getDist(getBounds()) <= bw * 50)
				pursuing = true;

			if (!pursuing)
				return;

			// then the mushroom is pursuing the player

			if (Level.curLevel.getPlayer().getX() > position.x) {
				// player is to the right
				facingRight = true;
				velocity.x = 2;
			}			
			else if (Level.curLevel.getPlayer().getX() < position.x) {				
				//player is to the left
				facingRight = false;
				velocity.x = -2;
			}
			
			if (this.state.equals(CreatureState.States.Attacking)) {
				// if the mushroom is attacking
				velocity.x = 0;
			}

		}
	}

	@Override
	public void attack() {
		// vector from mushroom to player
		if (Level.curLevel.getPlayer().getX() < position.x)
			// if the player is on the left
			facingRight = false;
		else
			facingRight = true;

		Vector2f dir = new Vector2f();
		Vector2f pPos = new Vector2f(Level.curLevel.getPlayer().centerPos());
		pPos.sub(new Vector2f(position), dir);
		dir.y = 0;

		Level.curLevel.addProjectile(new Fireball(centerPos().x, centerPos().y, dir, 7));
	}

}
