package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public class AttackingState extends CreatureState {

	@Override
	public void enter(Creature creature) {
		creature.curAnim = creature.attack;
		creature.velocity.x = 0;
	}

	@Override
	public CreatureState update(Creature creature) {
		if(creature.attack.getIndex() == creature.attack.length() - 1) {
			creature.attack.reset();
			return new IdleState();
		}
		return null;
	}

	@Override
	public States getState() {
		return States.Attacking;
	}

}
