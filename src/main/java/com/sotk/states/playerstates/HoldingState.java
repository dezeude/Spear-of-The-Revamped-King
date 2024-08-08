package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class HoldingState extends PlayerState {
	@Override
	public void enter(Player player) {
		player.curAnim = player.throwing;
	}

	@Override
	public void update(Player player) {
		if(player.throwing.getIndex() == 5)
			player.throwing.lock();
	}

}
