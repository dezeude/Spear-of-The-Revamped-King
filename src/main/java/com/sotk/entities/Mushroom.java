package com.sotk.entities;

import java.awt.image.BufferedImage;


import org.joml.Vector2f;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.states.creaturestates.CreatureState;

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
        attackRange = bw * 5;
        if (sheet == null)
            sheet = AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Idle.png");
        loadAnimations();
        curAnim = idle;
        facingRight = false;
        pursuingRange = 330;
    }

    private void loadAnimations() {
        idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
        run = new Animation(AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Run.png"), 0, 150, 150, 8, 0.2f);
        attack = new Animation(AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Attack.png"), 0, 150, 150, 8,
                0.15f);
        takeHit = new Animation(AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Take_Hit.png"), 0, 150, 150,
                4, 0.15f);
        death = new Animation(AssetsManager.loadImage("/sprites/mobs/enemies/mushroom/Death.png"), 0, 150, 150, 4,
                0.15f);

    }

    @Override
    public void update() {
        super.update();
        if (alive) {
            if (Level.curLevel.getPlayer().getDist(getBounds()) <= pursuingRange)
                pursuing = true;

            if (!pursuing)
                return;

            // then the mushroom is pursuing the player
            if (!this.state.equals(CreatureState.States.Invincible)) {
                if (this.state.equals(CreatureState.States.Attacking)) {
                    // if the mushroom is attacking
                    velocity.x = 0;
                } else if (Level.curLevel.getPlayer().getX() > position.x) {
                    // player is to the right
                    facingRight = true;
                    velocity.x = 2;
                } else if (Level.curLevel.getPlayer().getX() < position.x) {
                    // player is to the left
                    facingRight = false;
                    velocity.x = -2;
                }

            }
        }
    }

    @Override
    public void attack() {
        // if mushroom facing right, dir.x should be positive

        Vector2f dir = new Vector2f(facingRight ? 1 : -1, 0);

        Level.curLevel.addProjectile(new Fireball(centerPos().x, centerPos().y, dir, 6, this));
    }

}
