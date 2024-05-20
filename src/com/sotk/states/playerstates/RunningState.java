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
		if (player.bottom) {
			// set state to idle
			if (Math.abs(player.velocity.x) < 1.0f)
				player.setState(PlayerState.idle);
		} else
			player.setState(PlayerState.jumping);

	}

}
