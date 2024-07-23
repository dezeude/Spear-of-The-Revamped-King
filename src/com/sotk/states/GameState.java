package com.sotk.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import com.sotk.entities.Player;
import com.sotk.levels.*;
import com.sotk.main.GamePanel;
import com.sotk.main.Launcher;
import com.sotk.managers.TileMap;

public class GameState extends State {
	GamePanel game;
	Level curLevel;

	public GameState(GamePanel game) {
		this.game = game;
		init();
	}

	@Override
	public void init() {
		setLevel("level_1");
	}

	@Override
	public void update() {
		// moving up and down

		curLevel.update();

//		System.out.println("Width: " + game.getWidth() + " Height: " + game.getHeight());

	}

	@Override
	public void render(Graphics g) {
		// render the level
		curLevel.render(g);

	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void keyReleased(int key) {
		if (key == KeyEvent.VK_SPACE)
			curLevel.interact();
	}

	@Override
	public void mousePressed(int mouseBtn, int x, int y) {
		curLevel.mousePressed(mouseBtn, x, y);
	}

	@Override
	public void mouseReleased(int mouseBtn, int x, int y) {
		curLevel.mouseReleased(mouseBtn, x, y);
	}

	public void setLevel(String newLevel) {
		curLevel = new Level("/levels/" + newLevel + "/", this, game);
		Level.curLevel = curLevel;
	}

}
