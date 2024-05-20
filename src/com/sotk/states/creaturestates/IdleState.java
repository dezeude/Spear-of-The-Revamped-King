package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;
import com.sotk.levels.Level;

public class IdleState extends CreatureState {

	@Override
	public void enter(Creature creature) {
		// change animation
		creature.curAnim = creature.idle;
	}

	@Override
	public CreatureState update(Creature creature) {
		if (creature.bottom && creature.velocity.x != 0) {
			// set state to walking
			return new RunningState();
		}
		// creature take hit handled in creature class

		if (Level.curLevel.getPlayer().isAlive() && Level.curLevel.getPlayer().getDist(creature.getBounds()) <= 50) {
			// attacking the player if they're close enough
			return new AttackingState();
		}
		return null;
	}

	@Override
	public States getState() {
		return States.Idle;
	}

}
