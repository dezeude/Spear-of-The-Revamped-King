package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class IdleState extends PlayerState {

	@Override
	public void enter(Player player) {
		player.curAnim = player.run;
		
	}

	@Override
	public void update(Player player) {
		if(player.bottom) {
			if(player.velocity.x != 0) {
				//switch to running state
				player.state = PlayerState.running;
			}
		}
		else {
			//switch to jumping state
			player.state = PlayerState.jumping;
		}
		
	}

}
