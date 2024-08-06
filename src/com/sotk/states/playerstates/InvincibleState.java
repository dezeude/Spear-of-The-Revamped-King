package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class InvincibleState extends PlayerState {
	// This class represents the state a creature is when taking damage

	@Override
	public void enter(Player player) {
		// change animation
		player.curAnim = player.takeHit;
	}

	@Override
	public void update(Player player) {
		if (player.takeHit.getIndex() == player.takeHit.length() - 1) {
			player.velocity.x= 0; 
			player.setState(PlayerState.idle);
		}

		if (player.getHealth() <= 0) {
			// set the state to dead state
			player.setState(PlayerState.dead);
		}

	}

}
