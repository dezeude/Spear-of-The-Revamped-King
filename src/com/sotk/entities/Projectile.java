package com.sotk.entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;
import org.joml.Vector2i;

public abstract class Projectile extends Entity{
	protected float mass = 1.0f;
	protected BufferedImage sheet;
	
	public Vector2f velocity;
	public Vector2f resultantForce;
	
	public static Vector2f gravity = new Vector2f(0, .981f / 2);
	
	public Projectile(int x, int y, float vx, float vy, BufferedImage sheet) {
		position.set(x, y);
		velocity = new Vector2f();
		resultantForce = new Vector2f(vx, vy);
		this.sheet = sheet;
	}
	
	public Projectile(int x, int y, Vector2f direction, BufferedImage sheet) {
		position.set(x, y);
		resultantForce = new Vector2f(direction);
		this.velocity = new Vector2f();
		this.sheet = sheet;
	}
	
	public Projectile(Vector2i position, Vector2f direction, BufferedImage sheet) {
		this.position = new Vector2i(position);
		resultantForce = new Vector2f(direction);
		this.velocity = new Vector2f();
		this.sheet = sheet;
	}

	public void update() {
		Vector2f temp = new Vector2f();
		gravity.mul(mass, temp);
		applyForce(temp);
		
		resultantForce.div(mass);
		velocity.add(resultantForce);
		position.add((int)velocity.x, (int)velocity.y);
		resultantForce.zero();
	}
	
	public void applyForce(Vector2f force) {
		resultantForce.add(force.div(mass), resultantForce);
//		System.out.println("Force applied");
	}
	
	
}
