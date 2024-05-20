package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public class RunningState extends CreatureState {

	@Override
	public void enter(Creature creature) {
		// change animation
		creature.curAnim = creature.run;
	}

	@Override
	public CreatureState update(Creature creature) {
		if (creature.bottom && creature.velocity.x != 0) {
			// set state to idle
			return new IdleState();
		}
		return null;

	}

	@Override
	public States getState() {
		return States.Running;
	}

}
