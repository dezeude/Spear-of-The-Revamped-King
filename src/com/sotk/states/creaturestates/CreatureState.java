package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;

public abstract class CreatureState { // Instantiated FSM

	public enum States {
		Attacking, Dead, Idle, Invincible, Running
	}

	public abstract void enter(Creature creature); // enters the state

	public abstract CreatureState update(Creature creature);

	public abstract States getState();
	
	public boolean equals(States cs) {
		return cs == getState();
	}
}
