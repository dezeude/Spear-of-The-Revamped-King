package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class JumpingState extends PlayerState {
// This state handles both ascending and descending

	@Override
	public void enter(Player player) {
		if (player.velocity.y < 0)
			player.curAnim = player.jump;
		else
			player.curAnim = player.fall;
	}

	@Override
	public void update(Player player) {
		if (player.bottom) {
			// switch to idle state or running state
			if (player.velocity.x == 0)
				player.state = PlayerState.idle;
			else
				player.state = PlayerState.running;
		}
		if (player.velocity.y < 0)
			player.curAnim = player.jump;
		else
			player.curAnim = player.fall;

	}

}
