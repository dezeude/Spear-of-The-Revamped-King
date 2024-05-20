package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class IdleState extends PlayerState {

	@Override
	public void enter(Player player) {
		player.curAnim = player.idle;
		
	}

	@Override
	public void update(Player player) {
		if(player.bottom) {
			if(Math.abs(player.velocity.x) > 1) {
				//switch to running state
				player.setState(PlayerState.running);
			}
		}
		else {
			//switch to jumping state
			player.setState(PlayerState.jumping);
		}
		
	}

}
