package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public class IdleState extends CreatureState {

	@Override
	public void enter(Creature creature) {
		//change animation
		creature.curAnim = creature.idle;
	}

	@Override
	public CreatureState update(Creature creature) {
		if(creature.bottom && creature.velocity.x != 0) {
			//set state to walking
			return new RunningState();
		} 
		//creature take hit handled in creature class
		return null;
	}

	@Override
	public States getState() {
		return States.Idle;
	}


}
