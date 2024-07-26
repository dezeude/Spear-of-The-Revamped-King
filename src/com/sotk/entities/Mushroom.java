package com.sotk.entities;

import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.states.creaturestates.CreatureState;
import com.sotk.states.creaturestates.IdleState;

public class Mushroom extends Enemy {
	static BufferedImage sheet = null;
	public Animation throwAttack;

	public Mushroom(int x, int y) {
		xOff = 64;
		yOff = 64;
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		if (sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Idle.png");
		loadAnimations();
		curAnim = idle;
		this.state = new IdleState();
	}

	private void loadAnimations() {
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		run = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Run.png"), 0, 150, 150, 8, 0.2f);
		throwAttack = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Attack.png"), 0, 150,
				150, 8, 0.15f);
		takeHit = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Take_Hit.png"), 0, 150, 150,
				4, 0.15f);
		death = new Animation(AssetsManager.loadImage("/animations/mobs/enemies/mushroom/Death.png"), 0, 150, 150, 4,
				0.15f);

	}

	@Override
	public void update() {
		if (alive) {

			if (Level.curLevel.getPlayer().alive && Level.curLevel.getPlayer().getDist(getBounds()) <= 250) {
				// if the player is alive
				// move towards the player
				if (Level.curLevel.getPlayer().getDist(getBounds()) <= 20) {
					// the player is right in front of the goblin
					velocity.x = 0;
				} else if ((Level.curLevel.getPlayerBounds().x + Level.curLevel.getPlayerBounds().width)
						/ 2 > (this.position.x + this.bw) / 2) {
					// the player is on the right
					velocity.x = 2;
					facingRight = true;
				}

				else if ((Level.curLevel.getPlayerBounds().x + Level.curLevel.getPlayerBounds().width)
						/ 2 < (this.position.x + this.bw) / 2) {
					// the player is on the left
					velocity.x = -2;
					facingRight = false;
				}
			}

		}
		super.update();
	}

	public void processStates() {
		CreatureState state = this.state.update(this);
		if (state != null) {
			if (state.equals(CreatureState.States.Attacking)) {
				// change state to configured attacking
				state = new CreatureState() {

					@Override
					public void enter(Creature creature) {
						creature.curAnim = throwAttack;
					}

					@Override
					public CreatureState update(Creature creature) {
						if (throwAttack.isFinished()) {
							// vector from mushroom to player
							Vector2f dir = new Vector2f(Level.curLevel.getPlayer().centerPos());
							dir.sub(new Vector2f(creature.position));

							Level.curLevel.addProjectile(new Fireball(centerPos().x, centerPos().y, dir));
							throwAttack.reset();
							return new IdleState();
						}
						return null;
					}

					@Override
					public States getState() {
						return States.Special;
					}

				};
			}
			// if we're entering a new state
			curAnim.reset();
			this.state = state;
			this.state.enter(this);
		}
	}

}
