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

	public LinearProjectile(int x, int y, Vector2f direction, BufferedImage sheet) {
		super(x, y, direction.x, direction.y, sheet);
	}

	@Override
	public void update() {
		assert anim != null;
		position.add(7, 0);
		Rectangle bounds = new Rectangle(position.x, position.y, anim.getCurFrame().getWidth(),
				anim.getCurFrame().getHeight());
		Level.curLevel.enemyAttack(bounds, 1);

//		System.out.printf("X:%d, Y:%d\n", position.x, position.y);
	}

	@Override
	public void render(Graphics g) {
		assert anim != null;
		g.drawImage(anim.getCurFrame(), position.x - xOff - Camera.getXOffset(),
				position.y - yOff - Camera.getYOffset(), anim.getCurFrame().getWidth()+ 10, anim.getCurFrame().getHeight()+ 10,
				null);
		//only play the animation after the projectile has been hit.
//anim.play();
	}

	@Override
	public void addMetaData(String[] extras) {
		// MetaData not needed
	}
}
