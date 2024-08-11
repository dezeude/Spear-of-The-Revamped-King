package com.sotk.states.playerstates;

import com.sotk.entities.Player;

public class JumpingState extends PlayerState {
// This state handles both ascending and descending

    @Override
    public void enter(Player player) {
        RiseOrFall(player);
    }

    @Override
    public void update(Player player) {
        if (player.bottom) {
            // switch to idle state or running state
            if (player.velocity.x == 0)
                player.setState(PlayerState.idle);
            else
                player.setState(PlayerState.running);
            return;
        }

        RiseOrFall(player);

    }

    private void RiseOrFall(Player player) {
        if (player.velocity.y < 0)
            player.curAnim = player.jump;
        else
            player.curAnim = player.fall;

    }


}
