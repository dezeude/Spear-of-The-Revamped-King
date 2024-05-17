package com.sotk.main;

import java.util.Iterator;

import com.sotk.managers.Animation;

public class Debug {

	public static void main(String[] args) {
//		Animation anim = new Animation("/animations/player/Attack2.png", 5, 0.3f);
//		anim.setAttackFrame(0, 85, 37, 40, 60);
//		for(int i = 0; i < 20;i++) {
//			
//			anim.play();
//			System.out.println(anim.getIndex() + ": " + anim.isAttackFrame());
//			
//			
//		}
		
		float num = 0;
		for(int i = 0; i < 100; i++) {
			num += 0.3f;
			System.out.println("num: " + num);
			System.out.println("Cast: " + (int)num);
			System.out.println("Round: " + Math.round(num));
		}
		
//		System.out.println(anim.isAttackFrame());

	}

}
