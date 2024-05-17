package com.sotk.levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sotk.main.GamePanel;
import com.sotk.managers.Camera;
import com.sotk.managers.KeyManager;

public class Background {
	private ArrayList<BackImage> layers = new ArrayList<>();
//	private BufferedImage back, front;
//	private float bx,by,fx,fy;
//	private int yOff = 210;
//	AssetsManager assetsManager;
	GamePanel game;
	Color backgroundColor;
	KeyManager keyManager;
	
	public Background(Color color) {
		this.game = game;
		backgroundColor = color;
		keyManager = KeyManager.getInstance();
	}
	
	public void addLayer(BackImage bImg) {
		layers.add(bImg);
	}
	public void moveLeft() {
		for(BackImage b: layers)
			b.moveLeft();
	}
	public void moveRight() {
		for(BackImage b: layers)
			b.moveRight();
	}
	
	public void render(Graphics g) {
		//background color
		g.setColor(backgroundColor);
		g.fillRect(0, 0, GamePanel.getGraphicsWidth(), GamePanel.getGraphicsHeight());
		for(BackImage b: layers)
			b.render(g);
	
	}
}
