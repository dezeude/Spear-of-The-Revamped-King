package com.sotk.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.managers.AssetsManager;
import com.sotk.managers.Camera;

public class Spear extends Projectile{

	public Spear(int x, int y, float vx, float vy) {
		super(x, y, vx, vy, AssetsManager.loadImage("/animations/player/Spear.png"));
	}
	
	public Spear(int x, int y, Vector2f velocity) {
		super(x, y, velocity, AssetsManager.loadImage("/animations/player/Spear.png"));
	}
	
	public Spear(Vector2i position, Vector2f velocity) {
		super(position, velocity, AssetsManager.loadImage("/animations/player/Spear.png"));
	}


	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform transform = new AffineTransform();
		double theta = Math.atan2(velocity.y, velocity.x); 
		
		transform.rotate(theta);
	    transform = AffineTransform.getTranslateInstance(position.x - sheet.getWidth()/2 - Camera.getXOffset(), position.y - sheet.getHeight()/2 - Camera.getYOffset());
	    transform.rotate(theta, sheet.getWidth() / 2, sheet.getHeight() / 2);
	    g2d.drawImage(sheet, transform, null);
		
	}

	@Override
	public void addMetaData(String[] extras) {
		// TODO Auto-generated method stub
		
	}

}
