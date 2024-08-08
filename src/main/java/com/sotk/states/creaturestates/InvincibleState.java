package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public class InvincibleState extends CreatureState {
	// This class represents the state a creature is when taking damage

	@Override
	public void enter(Creature creature) {
		// change animation
		creature.curAnim = creature.takeHit;
		
	}

	@Override
	public CreatureState update(Creature creature) {
		if(creature.takeHit.isFinished()) {
			creature.takeHit.reset();
			return new IdleState();
		}

		if (creature.getHealth() <= 0) {
			// set the state to dead state
			return new DeadState();
		}

		return null;

	}

	@Override
	public States getState() {
		return States.Invincible;
	}

}
