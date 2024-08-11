package com.sotk.entities;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.levels.Level;
import com.sotk.main.GamePanel;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Camera;
import com.sotk.managers.TileMap;

public class Spear extends Projectile {
    private boolean stuck = false;
    private int stuckTimer = GamePanel.targetFPS * 3; // three seconds
    private final int stuckTimerMax = stuckTimer;

    public Spear(int x, int y, float vx, float vy, Creature owner) {
        super(x, y, vx, vy, AssetsManager.loadImage("/sprites/player/Spear.png"), owner);
    }

    public Spear(int x, int y, Vector2f velocity, Creature owner) {
        this(x, y, velocity.x, velocity.y, owner);

    }

    public Spear(Vector2i position, Vector2f velocity, Creature owner) {
        this(position.x, position.y, velocity.x, velocity.y, owner);
    }

    @Override
    public void update() {
        if (stuck) {
            if (stuckTimer == 0) {
                canRemove = true;
                return;
            }
            stuckTimer--;
            return;
        }

        super.update();

        // bounding box
        Vector2f temp = new Vector2f();
        velocity.normalize(temp);
        temp.mul(20, temp);
        Rectangle bounds = new Rectangle(position.x + (int) temp.x - 5, position.y + (int) temp.y - 5, 10, 10);

        if (Level.curLevel.damageEnemies(bounds, 1, this.owner))
            // spear is removed/deleted if enemy is hit
            canRemove = true;

        // check if spear hits collision map
        for (int i = position.y / TileMap.TILELENGTH - 2; i < (position.y + bounds.height) / TileMap.TILELENGTH
                + 2; i++) {
            for (int j = position.x / TileMap.TILELENGTH - 2; j < (position.x + bounds.width) / TileMap.TILELENGTH
                    + 2; j++) {
                if (i >= 0 && i < TileMap.map.length && j >= 0 && j < TileMap.map[i].length
                        && TileMap.map[i][j] - 1 > -1 && bounds.intersects(new Rectangle(j * TileMap.TILELENGTH,
                        i * TileMap.TILELENGTH, TileMap.TILELENGTH, TileMap.TILELENGTH)))
                    // make the spear stuck and prevent it from moving
                    stuck = true;
            }
        }

    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        float alpha = 1.0f;
        if (stuck)
            alpha = (float) stuckTimer / stuckTimerMax;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        AffineTransform transform = new AffineTransform();
        double theta = Math.atan2(velocity.y, velocity.x);

        transform.rotate(theta);
        transform = AffineTransform.getTranslateInstance(position.x - sprite.getWidth() / 2 - Camera.getXOffset(),
                position.y - sprite.getHeight() / 2 - Camera.getYOffset());

        transform.rotate(theta, sprite.getWidth() / 2, sprite.getHeight() / 2);

        // spear
        g2d.drawImage(sprite, transform, null);
    }

    @Override
    public void addMetaData(String[] extras) {
        // TODO Auto-generated method stub

    }

    public double calcYTrajecFromX(double x) {
        double angle = resultantForce.angle(new Vector2f(1, 0));
//		System.out.println(Math.toDegrees(angle));
        double leftSide = Math.tan(angle) * x;
        double rightSide = (gravity.y * Math.pow(x, 2))
                / (2.0 * Math.pow(resultantForce.length(), 2) * Math.pow(Math.cos(angle), 2));
        double y = leftSide - rightSide;
        return y;
    }

    // from equation
    public double TrajecRange() {
        double angle = resultantForce.angle(new Vector2f(1, 0));
        return (Math.pow(resultantForce.length(), 2) * Math.sin(2 * angle)) / gravity.y;
    }

}
