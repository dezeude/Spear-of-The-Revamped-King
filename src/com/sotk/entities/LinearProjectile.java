package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.Camera;

public abstract class LinearProjectile extends Projectile {
	protected Animation anim;
	protected int xOff, yOff;
	protected Vector2f direction;
	
	protected int radius;
	protected int lifeTime;

	public LinearProjectile(int x, int y, Vector2f direction, int speed) {
		super(x, y, direction.x, direction.y, null);
		this.direction = new Vector2f(direction.x, direction.y);
		this.direction.normalize(speed);
	}

	@Override
	public void update() {
		position.add((int) Math.round(direction.x), (int) Math.round(direction.y));
		
		lifeTime--;
		if (lifeTime <= 0)
			anim.unlock();
		
		if (anim.getIndex() == 0) {

			if (Level.curLevel.enemyAttack(new Rectangle(position.x, position.y, radius, radius), 1)) {
				anim.unlock();
				direction.zero();
			}
		} else if (anim.isFinished()) {
			canRemove = true;
		}
//the animation is only played when the projectile hits something or despawns.
		anim.play(); 
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(anim.getCurFrame(), position.x - xOff - Camera.getXOffset(),
				position.y - yOff - Camera.getYOffset(), anim.getCurFrame().getWidth() + 10,
				anim.getCurFrame().getHeight() + 10, null);
//		g.fillRect(position.x - Camera.getXOffset(), position.y - Camera.getYOffset(), 10, 10);

		// only play the animation after the projectile has been hit.
//anim.play();
	}

	@Override
	public void addMetaData(String[] extras) {
		// MetaData not needed
	}
}
