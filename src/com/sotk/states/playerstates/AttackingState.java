package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class AttackingState extends PlayerState {

	@Override
	public void enter(Player player) {
		player.curAnim = player.attack2;
		
	}

	@Override
	public void update(Player player) {
		if (player.attack2.getIndex() == player.attack2.length() - 1) {
			player.setState(PlayerState.attacking);
		}
		

	}

}
