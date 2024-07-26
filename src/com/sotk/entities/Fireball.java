package com.sotk.entities;

import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;

public class Fireball extends LinearProjectile {
	BufferedImage fireSprite = AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Projectile.png");

	public Fireball(int x, int y, Vector2f direction) {
		super(x, y, direction, null);
		anim = new Animation(fireSprite, 0, 50, 50, 8, 0.15f);
		xOff = 25;
		yOff = 25;
	}

}
