package com.sotk.entities;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.Camera;
import com.sotk.managers.KeyManager;
import com.sotk.states.playerstates.PlayerState;

public class Player extends Creature {
    private float maxSpeed = 5.0f;
    private int timeSinceJumped = 0;
    private boolean isJumping = false;

    KeyManager keyManager;

    public Animation jump;
    public Animation fall;
    public Animation throwing;

    long lastClicked;

    Level level;

    public PlayerState state;

    public Player(int x, int y, Level level) {
        // bounds x and y offsets, with the width and height
        xOff = 64;
        yOff = 57;
        // make the width and height of the gamePanel static
        position.set(x, y);
        bw = 20;
        bh = 40;
        health = 3;
        keyManager = KeyManager.getInstance();
        Camera.setDivisor(10);
        this.level = level;
        top = false;
        bottom = false;
        left = false;
        right = false;
        loadAnimations();
        curAnim = idle;
        this.state = PlayerState.idle;
        this.state.enter(this);
        speed = 5;
    }

    public void loadAnimations() {
        // load the idle animation
        idle = new Animation("/sprites/player/Idle.png", 8, 0.1f);

        // load the running animation
        run = new Animation("/sprites/player/Run.png", 8, 0.2f);

        // load the attack1 animation
//		attack1 = new Animation("/animations/player/Attack1.png", 5, 0.3f);

        // load the attack2 animation
        attack = new Animation("/sprites/player/Attack2.png", 5, 0.4f);
        attack.setAttackFrame(3, 85, 37, 40, 60);

        jump = new Animation("/sprites/player/Jump.png", 2, 0.2f);

        fall = new Animation("/sprites/player/Fall.png", 2, 0.2f);

        takeHit = new Animation("/sprites/player/Take hit.png", 3, 0.1f);
        death = new Animation("/sprites/player/Death.png", 8, 0.1f);

        throwing = new Animation("/sprites/player/Attack3.png", 7, 0.2f);
    }

    public void processInput() {
        if (keyManager.getKey(KeyEvent.VK_A)) {
            facingRight = false;
            velocity.x = -speed;
            if (keyManager.getKey(KeyEvent.VK_D)) velocity.x = 0;
        }

        if (keyManager.getKey(KeyEvent.VK_D)) {
            facingRight = true;
            velocity.x = speed;
            if (keyManager.getKey(KeyEvent.VK_A)) velocity.x = 0;
        }

        if (!keyManager.getKey(KeyEvent.VK_A) && !keyManager.getKey(KeyEvent.VK_D))
            velocity.x = 0;

        if (keyManager.getKey(KeyEvent.VK_W)) { // if w is being pressed
//			System.out.println(System.currentTimeMillis());
            if (state != PlayerState.invincible) {
                if (bottom) { // if there is a tile under the player

                    timeSinceJumped = 0;
                    applyForce(0, -12);
                    bottom = false;
                    isJumping = true;


                }
            }

        }

    }

    @Override
    public void update() {
        this.state.update(this);

        if (alive && !this.state.equals(PlayerState.invincible)) {
            processInput();

            Camera.smoothTo(position.x + bw / 2, position.y + bh / 2);// center player in the middle of the screen.
            // TODO: Fix SmoothTo
        }
        doPhysics();


        if (facingRight) {
            curFrame = curAnim.getCurFrame();

            if (curAnim.hasAttackFrame()) { // if the attack frame exists
                Rectangle curAttackFrame = curAnim.getAttackFrame();
                Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x,
                        position.y - yOff + curAttackFrame.y, curAttackFrame.width, curAttackFrame.height);
//				System.out.println(curAttackFrame);
                Level.curLevel.damageEnemies(newB, 1, this);
            }
        } else {
            curFrame = curAnim.getMirrorFrame();

            if (curAnim.hasAttackFrame()) { // if the attack frame exists
                Rectangle curAttackFrame = curAnim.getAttackFrame();
                Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x - bw - curAttackFrame.width,
                        position.y - yOff + curAttackFrame.y, curAttackFrame.width, curAttackFrame.height);
//				System.out.println(curAttackFrame);
                Level.curLevel.damageEnemies(newB, 1, this);
            }
        }

        curAnim.play();
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, bw, bh);
    }

    public double getDist(Rectangle other) {
        return Point2D.distance(position.x + bw / 2, position.y + bh / 2, other.x + other.width / 2,
                other.y + other.height / 2);
    }

    public float getVelX() {
        return velocity.x;
    }

    public float getVelY() {
        return velocity.y;
    }

    public void holdSpear() {
        setState(PlayerState.holding);
    }

    public void setThrowingState(int mouseX, int mouseY) {
        if (!this.state.equals(PlayerState.throwing)) {
            setState(PlayerState.throwing);
            this.throwing.unlock();
            PlayerState.throwing.setMousePosition(mouseX, mouseY);
        }
    }

    public void throwSpear(int mouseX, int mouseY) { // window points

        // convert window position to game position
        // compute vector between mouse and player bounds center
        Vector2f dir = new Vector2f((mouseX + Camera.getXOffset()) - (position.x + bw / 2),
                (mouseY + Camera.getYOffset()) - (position.y + bh / 2)).normalize(maxSpeed * 3);
        Vector2i newPos = centerPos();
        level.addProjectile(new Spear(newPos, dir, this));
    }

    @Override
    public void attack() {
        if (!this.state.equals(PlayerState.attacking))
            setState(PlayerState.attacking);
    }

    @Override
    public boolean damage(int dmg, Vector2f dir) {
        if (!state.equals(PlayerState.dead) && !state.equals(PlayerState.invincible)) {
            // damage the player
            health -= dmg;
//			System.out.printf("DIR: X:%f, Y:%f\n", dir.x, dir.y);
            curAnim.reset();
            applyKnockback(dir);
            state = PlayerState.invincible;
            state.enter(this);
            return true;
        }
        return false;
    }

    @Override
    public void die() {
        health = 0;
        alive = false;
    }

    public void setState(PlayerState ps) {
        if (state.equals(PlayerState.dead))
            return;

        curAnim.reset();
        this.state = ps;
        this.state.enter(this);
    }

    public PlayerState getState() {
        return this.state;
    }

}