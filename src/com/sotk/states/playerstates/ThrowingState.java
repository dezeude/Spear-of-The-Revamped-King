package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class ThrowingState extends PlayerState{

	@Override
	public void enter(Player player) {
		player.curAnim = player.throwing;
		
	}

	@Override
	public void update(Player player) {
		if (player.throwing.isFinished()) {
			player.setState(PlayerState.idle);
			
		}
		
	}

}
