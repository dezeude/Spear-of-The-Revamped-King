package com.sotk.states.creaturestates;

import com.sotk.entities.Creature;
import com.sotk.levels.Level;

public class RunningState extends CreatureState {

    @Override
    public void enter(Creature creature) {
        // change animation
        creature.curAnim = creature.run;
    }

    @Override
    public CreatureState update(Creature creature) {
        if (creature.bottom && Math.abs(creature.velocity.x) < 0.1f) {
            // set state to idle
            return new IdleState();
        }

        if (Level.curLevel.getPlayer().isAlive() && Level.curLevel.getPlayer().getDist(creature.getBounds()) <= creature.attackRange) {
            //attacking the player if they're close enough
            return new AttackingState();
        }

        return null;

    }

    @Override
    public States getState() {
        return States.Running;
    }

}
