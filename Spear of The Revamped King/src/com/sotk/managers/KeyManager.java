package com.sotk.managers;


public class KeyManager {
	private static KeyManager instance = new KeyManager();
	
	private boolean[] keys = new boolean[256];
	
	public static KeyManager getInstance() {
		return instance;
	}
	
	public boolean[] getKeys() {
		return keys;
	}
	
	public boolean getKey(int key) {
		//returns true if the key is being pressed
		return keys[key];
	}
	
	public void keyPressed(int key) {
		try {
			keys[key] = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		
}
	
	public void keyReleased(int key) {
		try { //in case of special inputs that exceed the array.
			keys[key] = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	
}
