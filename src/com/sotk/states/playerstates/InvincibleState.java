package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class InvincibleState extends PlayerState {
	// This class represents the state a creature is when taking damage

	@Override
	public void enter(Player player) {
		// change animation

	}

	@Override
	public void update(Player player) {
		if (!player.invincible) {
			// set the state to normal/idle?
			
		}

		if (player.getHealth() <= 0) {
			// set the state to dead state
			
		}

	}

}
