package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.levels.Level;
import com.sotk.managers.Animation;
import com.sotk.managers.Camera;
import com.sotk.managers.KeyManager;
import com.sotk.states.playerstates.PlayerState;

public class Player extends Creature {
//	private int renderWidth = 150, renderHeight = 150;
	private int xOff = 64,yOff = 57; //bounds x and y offsets, with the width and height 
	private float maxSpeed = 5.0f;
	private int timeSinceJumped = 0;
	private boolean isJumping = false;
	private final float knockBack = 5.0f;
	private int renderDimensions = 150;
	private int mouseX = 0;
	private int mouseY = 0;

	KeyManager keyManager;
	
	//animations
//	AnimationManager animationManager;
	public Animation curAnim;
	public Animation idle;
	public Animation run;
//	Animation attack1;
	public Animation attack2;
	public Animation jump;
	public Animation fall;
	public Animation takeHit;
	public Animation death;
	BufferedImage curFrame;
	
	boolean inAnimation = false; //for important animations that cannot be cancelled until finished
//	boolean attackQ = false;
	
	long lastClicked;
	
	Level level;
	
	public PlayerState state;
	
	public Player(int x, int y, Level level) {
		//make the width and height of the gamePanel static
		velocity = new Vector2f(x,y);
		bw = 20;
		bh = 40;
		health = 3;
		keyManager = KeyManager.getInstance();
		Camera.setDivisor(10);
		this.level = level;
//		animationManager = new AnimationManager();
		state = PlayerState.idle;
		top = false;
		bottom = false;
		left = false;
		right = false;
		loadAnimations();
	}
	
	public void loadAnimations() {
		//load the idle animation
		idle = new Animation("/animations/player/Idle.png", 8, 0.1f);		
		
		//load the running animation
		run = new Animation("/animations/player/Run.png", 8, 0.2f);
		
		//load the attack1 animation
//		attack1 = new Animation("/animations/player/Attack1.png", 5, 0.3f);
		
		
		//load the attack2 animation
		attack2 = new Animation("/animations/player/Attack2.png", 5, 0.4f);
		attack2.setAttackFrame(3, 85, 37, 40, 60);
		
		jump = new Animation("/animations/player/Jump.png", 2, 0.2f);
		
		fall = new Animation("/animations/player/Fall.png", 2, 0.2f);
		
		takeHit = new Animation("/animations/player/Take hit.png", 3, 0.1f);
		death = new Animation("/animations/player/Death.png", 8, 0.1f);
	}
	
	
	public void processInput() { 
		velocity.x = 0;
		if(keyManager.getKey(KeyEvent.VK_A)) {
			if(!inAnimation || attacking) {
				if(!attacking)
					facingRight = false;
				velocity.x += -5;
				if(velocity.x < maxSpeed)
					velocity.x = -maxSpeed;
			}
			
		}
			
		if(keyManager.getKey(KeyEvent.VK_D)) {
			if(!inAnimation || attacking) {
				if(!attacking)
					facingRight = true;
				velocity.x += 5;
				if(velocity.x > maxSpeed)
					velocity.x = maxSpeed;
			}
			
		}
			
		if(keyManager.getKey(KeyEvent.VK_W)) { //if w is being pressed
//			System.out.println(System.currentTimeMillis());
			if(!invincible) {
				if(bottom) { //if there is a tile under the player
					timeSinceJumped = 0;
					velocity.y = -9;
					bottom = false;
					isJumping = true;
				}
				else { //if the player is in the air
					timeSinceJumped++;
					if(timeSinceJumped <= 20 && isJumping) {
						velocity.y -= 0.3;
					}
					else {
						timeSinceJumped = 0;
						isJumping = false;
					}
					
				}
			}
				
		}
		if(!keyManager.getKey(KeyEvent.VK_W) && !bottom)
// if the w isn't held and the player is in the air.
			isJumping = false;
// the player isn't jumping anymore			
		
		if(bottom) {//if the player is standing on a tile
			//change the y velocity so the player falls slower
			velocity.y = 1;
		}
		else {//if player is falling
			
			velocity.y += 0.45f;
			//apply gravity
		}
		if(velocity.x > 0) { //player moving right
			if(velocity.x <= 1)
				velocity.x = 0;
			else
			velocity.x--;
		} 
		if(velocity.x < 0) { //player moving left
			if(velocity.x >= -1)
				velocity.x = 0;
			else
			velocity.x++;
		}
		
		top = false;
		bottom = false;
		left = false;
		right = false;
		
		force.div(mass);
		velocity.add(force);	
//		velocity.mul(dt);	
		force.zero();
		
		move((int)velocity.x,(int)velocity.y); //move the player and check for collisions
		
	}	
	
	@Override
	public void update() {
		processStates();
		if(alive) {
			
		processInput();
		Camera.smoothTo(position.x+bw/2,position.y+bh/2);//center player in the middle of the screen.
		//TODO: Fix SmoothTo
		if(inAnimation) {
			if(invincible) {
				curAnim = takeHit;
				if(takeHit.getIndex() == takeHit.length() - 1) {
					resetAnims();
					takeHit.reset();
				}
				
			}
			else if(attacking) {
				//change the player's Frame depending on the state of the player
				curAnim = attack2;
				if(attack2.getIndex() == attack2.length() - 1) {
					resetAnims();
					attack2.reset();
				}	
				
			}
			else
				resetAnims();
			
		}
		else {
			if(bottom) { //if the player is on a tile/on the ground
				if(velocity.x == 0) {
					curAnim = idle;
				}
				else {
					curAnim = run;
				}
			}
			else {
				if(velocity.y < 0) { //player is ascending
					curAnim = jump;
				}
				else {
					curAnim = fall;
				}
				
			}
			
		}
		
		}
		else {
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
				level.damageEnemies(newB, 1);
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
				level.damageEnemies(newB, 1);
			}
		}
		
		curAnim.play();
		

	}
	
	@Override
	public void render(Graphics g) {
//		g.fillRect(position.x - xOff - Camera.getXOffset(),
//				   position.y - yOff - Camera.getYOffset(),
//				   renderDimensions,
//				   renderDimensions);
		//draw the player
		g.drawImage(curFrame,
					position.x - xOff - Camera.getXOffset(),
					position.y - yOff - Camera.getYOffset(),
					renderDimensions,
					renderDimensions,
					null);
//		g.drawLine(position.x + bw/2 - Camera.getXOffset(), position.y+bh/2 - Camera.getYOffset(), mouseX, mouseY);
		
	}
	
	public void resetAnims() {
		attacking = false;
		inAnimation = false;
		invincible = false;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(position.x,position.y,bw,bh);
	}
	
	public double getDist(Rectangle other) {
		return Point2D.distance(position.x + bw/2,
								position.y + bh/2,
								other.x + other.width/2,
								other.y + other.height/2);
	}
	
	public void attack() {
		if(!attacking && !inAnimation) {//if the player is not attacking
			attacking = true;
			inAnimation = true;
		}
			
		else { //if the player is attacking
			//let the player attack depending on how long ago the player clicked
			lastClicked = System.nanoTime();
		}
		
		
			
	}
	
	public float getVelX() {
		return velocity.x;
	}
	
	public float getVelY() {
		return velocity.y;
	}


	@Override
	public void die() {
		resetAnims();
		inAnimation = true;
		alive = false;
	}

	@Override
	public void takeHit() {
		attacking = false;
		invincible = true;
		inAnimation = true;
		attack2.reset();
		health = 3;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void addForce(Vector2f force) {
		force.normalize(force);
		force.mul(knockBack, force);
		this.force.add(force);
		force.zero();
	}
	
	public void addForce(Vector2i force) {
		addForce(new Vector2f((float)force.x, (float)force.y));
	}

	public void throwSpear(int x, int y) {
		mouseX = x;
		mouseY = y;

//		System.out.println("pX: " + position.x + " pY: " + position.y);
		Vector2f dir = new Vector2f((mouseX + Camera.getXOffset()) - (position.x + bw/2),
									(mouseY + Camera.getYOffset()) - (position.y + bh/2)).normalize(maxSpeed * 3);
//		System.out.println("DirX: " + dir.x() + " DirY: " + dir.y());
		Vector2i newPos = centerPos();
		level.addProjectile(new Spear(newPos, dir));
	}
	
	public void throwSpear(Vector2i point) {
		throwSpear(point.x, point.y);
	}
	
	public Vector2i centerPos() {
		return new Vector2i(position.x + bw/2, position.y + bh/2);
	}


}