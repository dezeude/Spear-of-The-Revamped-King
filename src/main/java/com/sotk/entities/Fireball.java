package com.sotk.entities;

import java.awt.image.BufferedImage;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;

public class Fireball extends LinearProjectile {
    BufferedImage fireSprite = AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Projectile.png");

    public Fireball(int x, int y, Vector2f direction, int speed, Creature owner) {
        super(x, y, direction, speed, owner);
        anim = new Animation(fireSprite, 0, 50, 50, 8, 0.15f);
        xOff = 25;
        yOff = 25;
        anim.lock();
        radius = 10;
        lifeTime = 80;
    }

    public Fireball(Vector2i position, Vector2f direction, int speed, Creature owner) {
        this(position.x, position.y, direction, speed, owner);
    }

}
