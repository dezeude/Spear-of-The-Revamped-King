package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class RunningState extends PlayerState {

	@Override
	public void enter(Player player) {
		// change animation
		player.curAnim = player.run;
	}

	@Override
	public void update(Player player) {
		if (player.bottom && player.velocity.x == 0) {
			// set state to idle
			player.state = PlayerState.idle;
		}

	}

}
