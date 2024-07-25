package com.sotk.entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sotk.levels.Level;

public abstract class Enemy extends Creature {
	@Override
	public void update() {
		processStates();
		doPhysics();
		if (facingRight) {
			curFrame = curAnim.getCurFrame();

			if (curAnim.hasAttackFrame()) { // if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x,
						position.y - yOff + curAttackFrame.y, curAttackFrame.width, curAttackFrame.height);
//				System.out.println(curAttackFrame);
				Level.curLevel.enemyAttack(newB, 1);
			}
		} else {
			curFrame = curAnim.getMirrorFrame();

			if (curAnim.hasAttackFrame()) { // if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x - bw - curAttackFrame.width,
						position.y - yOff + curAttackFrame.y, curAttackFrame.width, curAttackFrame.height);
//				System.out.println(curAttackFrame);
				Level.curLevel.enemyAttack(newB, 1);
			}
		}

		curAnim.play();
	}
}
