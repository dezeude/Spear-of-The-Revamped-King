package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.managers.Animation;
import com.sotk.managers.TileMap;
import com.sotk.states.creaturestates.AttackingState;
import com.sotk.states.creaturestates.CreatureState;
import com.sotk.states.creaturestates.DeadState;
import com.sotk.states.creaturestates.IdleState;
import com.sotk.states.creaturestates.InvincibleState;

public abstract class Creature extends Entity {
	public boolean top = false, bottom = false, left = false, right = false;
	protected int bw, bh, health;
//	protected float velx,vely;
//	protected float gravity = 0.45f;
	public boolean facingRight = true;// , invincible = false;
	public boolean alive = true;
	protected String[] extras;

	public Vector2f velocity = new Vector2f();
	protected Vector2f force = new Vector2f(); // resultant force
	protected float mass = 1.0f;// default kg

	public static final Vector2f gravity = new Vector2f(0, 9.81f);

	private CreatureState state = new IdleState();

	public boolean canRemove = false; // field for when the creature can be despawned.

	// animations
	public Animation curAnim;
	public Animation idle;
	public Animation run;
	public Animation takeHit;
	public Animation death;
	public Animation attack;

	public void processStates() {
		CreatureState state = this.state.update(this);
		// null state means our state didn't change
		if (state != null) {
			// if we're entering a new state
			curAnim.reset();
			this.state = state;
			this.state.enter(this);
		}
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
		for (int i = position.y / TileMap.TILELENGTH - 2; i < (position.y + bh) / TileMap.TILELENGTH + 2; i++) {
			for (int j = position.x / TileMap.TILELENGTH - 2; j < (position.x + bw) / TileMap.TILELENGTH + 2; j++) {
				if (i >= 0 && i < TileMap.map.length && j >= 0 && j < TileMap.map[i].length
						&& TileMap.map[i][j] - 1 > -1)
					hitList.add(new Rectangle(j * TileMap.TILELENGTH, i * TileMap.TILELENGTH, TileMap.TILELENGTH,
							TileMap.TILELENGTH));
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
		if (!state.equals(CreatureState.States.Invincible)) {
			health -= dmg;
			System.out.println(health);
			curAnim.reset();
			state = new InvincibleState();
			state.enter(this);
		}

	}

	public void die() {
		curAnim.reset();
		state = new DeadState();
		state.enter(this);
	}

//	public abstract void attack(Rectangle hitbox, int damage);
	// add a hitbox to the level with the damage

	public void addMetaData(String[] data) {
		this.extras = data;
	}

	public void attack() {
		curAnim.reset();
		state = new AttackingState();
		state.enter(this);
	}

	public Vector2i centerPos() {
		return new Vector2i(position.x + bw / 2, position.y + bh / 2);
	}

	public boolean isAlive() {
		return alive;
	}
	
	public abstract void doPhysics();
}
