package com.sotk.states;

public abstract class State{
	public abstract void init();
	public abstract void update();
	public abstract void render(java.awt.Graphics g);
	public abstract void keyPressed(int key);
	public abstract void keyReleased(int key);
	public abstract void mousePressed(int mouseBtn, int x, int y);
	public abstract void mouseReleased(int mouseBtn);

}
