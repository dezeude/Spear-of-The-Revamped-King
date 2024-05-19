package com.sotk.states;

import com.sotk.entities.Creature;

public abstract class CreatureState { //Instantiated FSM
	public abstract void enter(Creature creature); // enters the state

	public abstract CreatureState update(Creature creature);

}
