package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.AssetsManager;
import com.sotk.managers.Bound;
import com.sotk.managers.Camera;
import com.sotk.managers.Collisions;
import com.sotk.states.creaturestates.AttackingState;

public class Goblin extends Enemy {
	//position offsets
	private int xOff = 64, yOff = 64; //placeholder values
	//render bounds
//	private int renderWidth = 150, renderHeight = 150;
	//sprite sheet
	private static BufferedImage sheet;
	BufferedImage curFrame;
	Level level;
	
//	private static Goblin firstGoblin;
			
	public Goblin(int x, int y, Level level) {
		position.x = x;
		position.y = y;
		bw = 22;
		bh = 36;
		health = 3;
		this.level = level;
		if(sheet == null)
			sheet = AssetsManager.loadImage("/animations/mobs/enemies/goblin/goblinSprite.png");
		loadAnimations();
		curAnim = idle;
	}
	
//	public Goblin newGoblin(int x, int y, Level level) {
////*******************************************
////APPLY THIS TECHNIQUE TO ALL THE ENEMIES
//
//		position.x = x;
//		position.y = y;
//		bw = 22;
//		bh = 36;
//		this.level = level;
////		if(sheet == null)
////			sheet = AssetsManager.loadImage("/mobs/enemies/goblin/goblinSprite.png");
//		
//		if(firstGoblin == null) { //if this is the first instance of the goblin
//			firstGoblin = this;
//			loadAnimations();
//			return this;
//		}
//		//if not, share the image frames, to save memory
//		idle = new Animation(firstGoblin.idle);
//		run = new Animation(firstGoblin.run);
//		return this;
//		
//	}
//	
	private void loadAnimations() {
		idle = new Animation(sheet, 0, 150, 150, 4, 0.1f);
		run = new Animation(sheet, 1, 150, 150, 8, 0.2f);
		takeHit = new Animation(sheet, 5, 150, 150, 4, 0.14f);
//		takeHit = new Animation(sheet, 3, 150, 8, 0.14f);
		death = new Animation(sheet, 6, 150, 150, 4, 0.14f);
		attack = new Animation(sheet, 2, 150, 150, 8, 0.14f);
		attack.setAttackFrame(6, 80, 71, 40, 30);
	}
	

	@Override
	public void update() {
		processStates();
		if(alive){
			velocity.x = 0;
			if(level.getPlayer().alive && level.getPlayer().getDist(getBounds()) <= 250) {//if the player is alive
				//moving towards the player
				if(level.getPlayerBounds().x > this.position.x + this.bw) { 
					//the player is on the right
					velocity.x = 2;
//					if(!inAnimation)
						facingRight = true;
				}
					
				else if(level.getPlayerBounds().x + level.getPlayerBounds().width < this.position.x) {
					//the player is on the left
					velocity.x = -2;
//					if(!inAnimation)
						facingRight = false;
				}
				
				if(level.getPlayer().getDist(getBounds()) <= 50) {
					//attacking the player if they're close enough
//					if(!attacking && !inAnimation) {//if the goblin is not attacking
//						attack();
//					}
					attack();
				}
			}
			
				
			
			if(!bottom)
				velocity.y += 0.45f;
			else
				velocity.y = 1;
			
			top = false;
			bottom = false;
			left = false;
			right = false;
			
			move((int)velocity.x,(int)velocity.y);
			
			
		}
		else {//if the goblin is dead
			curAnim = death;
			if(death.getIndex() == death.length() - 1)
				death.lock();
		}
		
		if(facingRight) {
			curFrame = curAnim.getCurFrame();
			
			if(curAnim.hasAttackFrame()) { //if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x,
											   position.y - yOff + curAttackFrame.y,
											   curAttackFrame.width,
											   curAttackFrame.height);
//				System.out.println(curAttackFrame);
				level.enemyAttack(newB, 1);
			}
			
		}
			
		else {
			curFrame = curAnim.getMirrorFrame();
			
			if(curAnim.hasAttackFrame()) { //if the attack frame exists
				Rectangle curAttackFrame = curAnim.getAttackFrame();
				Rectangle newB = new Rectangle(position.x - xOff + curAttackFrame.x - bw - curAttackFrame.width,
											   position.y - yOff + curAttackFrame.y,
											   curAttackFrame.width,
											   curAttackFrame.height);
//				System.out.println(curAttackFrame);
				level.enemyAttack(newB, 1);
			}
		}

		curAnim.play();
	}

	@Override
	public void render(Graphics g) {
		
		g.drawImage(curFrame, position.x - xOff - Camera.getXOffset(), position.y - yOff - Camera.getYOffset(), curFrame.getWidth(), curFrame.getHeight(), null);

		
		//draw line from middle of goblin to middle of player
//		g.drawLine(bx + bw/2 - Camera.getXOffset(), by + bh/2 - Camera.getYOffset(),
//					level.getPlayerBounds().x + level.getPlayerBounds().width/2 - Camera.getXOffset(),
//					level.getPlayerBounds().y + level.getPlayerBounds().height/2 - Camera.getYOffset());
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(position.x,position.y,bw,bh);
	}


}
