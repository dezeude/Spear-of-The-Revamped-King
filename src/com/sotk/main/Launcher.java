package com.sotk.main;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import javax.swing.JFrame;

import com.sotk.managers.AssetsManager;

public class Launcher {

	public static JFrame frame;
	public static BufferedImage icon;

	public static void main(String[] args) {
		icon = AssetsManager.loadImage("/misc/Spear.png");
		frame = new JFrame("Spear of the King");
		frame.setIconImage(icon);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		Image cursorIcon = AssetsManager.loadImage("/misc/cursor.png");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor c = toolkit.createCustomCursor(cursorIcon, new Point(frame.getX(), frame.getY()), "custom cursor");
		frame.setCursor(c);
		GamePanel gp = new GamePanel();
		frame.setContentPane(gp);
		frame.addKeyListener(gp);
		frame.setLayout(null);
		frame.pack();
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

}
