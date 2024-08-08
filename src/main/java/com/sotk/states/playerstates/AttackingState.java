package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class AttackingState extends PlayerState {

	@Override
	public void enter(Player player) {
		player.curAnim = player.attack;
		
	}

	@Override
	public void update(Player player) {
		if (player.attack.isFinished()) {
			player.setState(PlayerState.idle);
		}
		

	}

}
