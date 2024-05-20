package com.sotk.entities;

import org.joml.Vector2i;

public abstract class Entity {
	protected Vector2i position = new Vector2i();
	public abstract void update();
	public abstract void render(java.awt.Graphics g);
	public int getX() {return position.x;}
	public int getY() {return position.y;}
	public abstract void addMetaData(String[] extras);
}
