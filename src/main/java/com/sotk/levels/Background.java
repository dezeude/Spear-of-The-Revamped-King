package com.sotk.levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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
	Color backgroundColor;
	KeyManager keyManager;
	
	public Background(Color color) {
		backgroundColor = color;
		keyManager = KeyManager.getInstance();
	}
	
	public void addLayer(BackImage bImg) {
		layers.add(bImg);
	}
	public void addLayers(ArrayList<BackImage> bImgs){
		layers.addAll(bImgs);
	}

	/**
	 add all the images in the path to the background, using their names (or regex) as the backImage speed parameters
	 @param path The path of the directory containing all the layer images
	 @param regex The regular expression that corresponds to the speed of the backimage
	 */
	public void addLayerFolder(String path, String regex) {
		//Don't think this is needed
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
