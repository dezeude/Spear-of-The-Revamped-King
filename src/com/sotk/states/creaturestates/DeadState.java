package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public class DeadState extends CreatureState {
	private int deadTime; // # of frames since dead

	@Override
	public void enter(Creature creature) {
		// change animation
		creature.curAnim = creature.death;
		creature.alive = false;
		deadTime = 0;
	}

	@Override
	public CreatureState update(Creature creature) {
		deadTime++;
		if (deadTime > 300) { // 5 seconds
			// despawn creature after it's been dead for a certain amount of time.
			creature.canRemove = true;
		}
		
		if(creature.death.isFinished())
			creature.death.lock();
		
		return null;

	}

	@Override
	public States getState() {
		return States.Dead;
	}

}
