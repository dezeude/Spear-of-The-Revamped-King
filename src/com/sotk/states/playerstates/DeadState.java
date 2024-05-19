package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class DeadState extends PlayerState {
	private int deadTime; // # of frames since dead

	@Override
	public void enter(Player player) {
		// change animation

		player.alive = false;
		deadTime = 0;
	}

	@Override
	public void update(Player player) {
		deadTime++;
		if (deadTime > 300) { // 5 seconds
			// despawn creature after it's been dead for a certain amount of time.
			player.canRemove = true;
		}

	}

}
