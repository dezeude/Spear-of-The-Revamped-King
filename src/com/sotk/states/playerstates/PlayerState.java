package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public abstract class PlayerState { // Static FSM
	// do we need the enter method?
	// According to https://gameprogrammingpatterns.com/state.html#a-state-interface
	// I don't think an enter method is needed for Static FSM.

	public static DeadState dead = new DeadState();
	public static InvincibleState invincible = new InvincibleState();
	public static IdleState idle = new IdleState();
	public static RunningState running = new RunningState();
	public static JumpingState jumping = new JumpingState();
	public static AttackingState attacking = new AttackingState();
	public static ThrowingState throwing = new ThrowingState();

	public abstract void enter(Player player); // enters the state

	public abstract void update(Player player);
}
