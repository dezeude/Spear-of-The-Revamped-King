package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.managers.TileMap;
import com.sotk.states.CreatureState;
import com.sotk.states.IdleState;

public abstract class Creature extends Entity {
	public boolean top = false, bottom = false, left = false, right = false;
	protected int bw, bh, health;
	private int TILELENGTH = TileMap.TILELENGTH;
//	protected float velx,vely;
//	protected float gravity = 0.45f;
	public boolean facingRight = true, inAnimation = false, invincible = false, alive = true, attacking = false;
	protected String[] extras;

	public Vector2f velocity = new Vector2f();
	protected Vector2f force = new Vector2f(); // resultant force
	protected float mass = 1.0f;// default kg

	public static final Vector2f gravity = new Vector2f(0, 9.81f);

	private CreatureState state = new IdleState();

	public boolean canRemove = false; // field for when the creature can be despawned.

	public void processStates() {
		CreatureState state = this.state.update(this);

		this.state = state;
		if (!this.state.getClass().equals(state.getClass()))
			// if we're entering a new state
			state.enter(this);
	}

	public void setState(CreatureState state) {
		this.state = state;
	}

	public CreatureState getState() {
		return state;
	}

	@Override
	public abstract void update();

	@Override
	public abstract void render(Graphics g);

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return (int) position.x();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return (int) position.y();
	}

	public Vector2i getPos() {
		return position;
	}

	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, bw, bh);
	}

	public void move(float vx, float vy) {
		top = false;
		bottom = false;
		left = false;
		right = false;
		// get all the possible tiles in a list
		ArrayList<Rectangle> hitList = new ArrayList();
		for (int i = position.y / TILELENGTH - 2; i < (position.y + bh) / TILELENGTH + 2; i++) {
			for (int j = position.x / TILELENGTH - 2; j < (position.x + bw) / TILELENGTH + 2; j++) {
				if (i >= 0 && i < TileMap.map.length && j >= 0 && j < TileMap.map[i].length
						&& TileMap.map[i][j] - 1 > -1)
					hitList.add(new Rectangle(j * TILELENGTH, i * TILELENGTH, TILELENGTH, TILELENGTH));
			}
		}

		// calculate collisions on the x-axis first
		position.x += vx;
		Rectangle newBounds = new Rectangle(position.x, position.y, bw, bh);
		for (Rectangle r : hitList) {
			if (newBounds.intersects(r)) {
//				System.out.println("collision found on x-axis");
				if (vx > 0) { // if the tile is on the right of the player
					position.x = r.x - newBounds.width;
					right = true;
					velocity.x = 0;
				}
				if (vx < 0) {// if the tile is on the left of the player
					position.x = r.x + r.width;
					left = true;
					velocity.x = 0;
				}
			}
		}

		// calculate collisions on the y-axis second
		position.y += vy;
		newBounds = new Rectangle(position.x, position.y, bw, bh);
		for (Rectangle r : hitList) {
			if (newBounds.intersects(r)) {
//				System.out.println("collision found on y-axis");
				if (vy > 0) { // if the tile is under the player
					position.y = r.y - newBounds.height;
					bottom = true;
					velocity.y = 0;
				}
				if (vy < 0) {// if the tile is above the player
					position.y = r.y + r.height;
					top = true;
					velocity.y = 0;
				}
			}
		}

	}

	public int getHealth() {
		return health;
	}

	public void damage(int dmg) {
		// set the state to invincible state if not already there
		if (!invincible) {
			invincible = true;
			inAnimation = true;
			// play the damage animation
			health -= dmg;
			takeHit();
			if (health <= 0) {
				die();
			}
		}

	}

	public void die() {
		inAnimation = true;
		alive = false;
	}

	public abstract void takeHit();

//	public abstract void attack(Rectangle hitbox, int damage);
	public abstract void attack();
	// add a hitbox to the level with the damage

	public void addExtras(String[] extras) {
		this.extras = extras;
	}

}
