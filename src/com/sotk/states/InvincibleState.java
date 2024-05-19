package com.sotk.states;

import com.sotk.entities.Creature;

public class InvincibleState extends CreatureState {
	// This class represents the state a creature is when taking damage

	@Override
	public void enter(Creature creature) {
		// change animation

	}

	@Override
	public CreatureState update(Creature creature) {
		if (!creature.invincible) {
			// set the state to normal/idle?
			return new IdleState();
		}

		if (creature.getHealth() <= 0) {
			// set the state to dead state
			return new DeadState();
		}

		return this;

	}

}
