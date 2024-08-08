package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class ThrowingState extends PlayerState {
	int mouseX = 0, mouseY = 0;

	@Override
	public void enter(Player player) {
		player.curAnim = player.throwing;
		player.throwing.setIndex(5);
	}

	@Override
	public void update(Player player) {
		if (player.throwing.isFinished()) {
			player.setState(PlayerState.idle);
			player.throwSpear(mouseX, mouseY);
		}

	}

	public void setMousePosition(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}

}
