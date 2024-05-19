package com.sotk.states;

import com.sotk.entities.Creature;

public class WalkingState extends CreatureState {

	@Override
	public void enter(Creature creature) {
		// change animation

	}

	@Override
	public CreatureState update(Creature creature) {
		if (creature.bottom && creature.velocity.x != 0) {
			// set state to idle
			return new IdleState();
		}
		return this;

	}

}