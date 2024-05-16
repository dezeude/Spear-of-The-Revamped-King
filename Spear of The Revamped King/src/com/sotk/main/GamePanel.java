package com.sotk.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector2i;

import com.sotk.managers.KeyManager;
import com.sotk.managers.TileMap;
import com.sotk.states.GameState;
import com.sotk.states.State;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, FocusListener{
	public final int bufferWidth = TileMap.TILELENGTH * 25;
	public final int bufferHeight = TileMap.TILELENGTH * 14;
	private Thread thread;
	private boolean running;
	private State gameState;
	private KeyManager keyManager;
	private Graphics gOffscreen;
	//offscreen graphics
	private static BufferedImage offScreenBuffer;
	//the buffer that is scaled and painted to the screen/Panel.
	public static int targetFPS = 60;
	
	public GamePanel() {
		super();
		setSize(bufferWidth, bufferHeight);
		setPreferredSize(new Dimension(bufferWidth, bufferHeight));
		setBackground(Color.black);
		init();
	}
	
	public void init() {
		addKeyListener(this);
		addMouseListener(this);
		addFocusListener(this);
		offScreenBuffer = new BufferedImage(bufferWidth, bufferHeight, BufferedImage.TYPE_INT_ARGB);
		running = true;
		gameState = new GameState(this);
		keyManager = KeyManager.getInstance();
		thread = new Thread(this);
		thread.start();
	}	
	
	@Override
	public void run() {
//		int fps = 60;
//		long start;
//		long elapsed;
//		long wait;
//		long targetTime = 1000 / fps;
		//Notch way
		int oneSecond = 1000000000;
		float timePerTick = oneSecond / targetFPS;
		float delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			//Notch way
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			
			if(delta >= 1){
				ticks++;
				--delta;
//				System.out.println(dt);
				update();
				repaint();
			}
			
			if(timer >= oneSecond){
//				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
			
			
		}
		
	}
	
	public void update() {
		gameState.update();
	}
	
	public static int getGraphicsWidth() {
		return offScreenBuffer.getWidth();
	}
	
	public static int getGraphicsHeight() {
		return offScreenBuffer.getHeight();
	}
	/*
	 * Converts the coordinates from the window to coordinates in game.
	 * <p>
	 * @param	coordinate from the window.
	 * @return	Returns the window coords in game coordinates 
	 */
	public Vector2i windowToBufferPoint(Vector2i windowPoint) {
		//try double.
		float widthScaleFactor = bufferWidth / (float)getWidth();
		float heightScaleFactor = bufferHeight / (float)getHeight();
		
//		System.out.println("Width: " + widthScaleFactor);
//		System.out.println("Height: " + heightScaleFactor);
		
		int x = Math.round(windowPoint.x * widthScaleFactor);
		int y = Math.round(windowPoint.y * heightScaleFactor);

		return new Vector2i(x,y);
	}
	
	
//	public void drawToOffScreen() {
//		gOffscreen = offScreenBuffer.getGraphics();
//		gOffscreen.setColor(Color.white);
//		gOffscreen.fillRect(0, 0, offScreenBuffer.getWidth(), offScreenBuffer.getHeight());
//		gameState.render(gOffscreen);
//		gOffscreen.dispose();
//	}
//	
//	public void drawToScreen() {
//		gScreen = getGraphics();
//		gScreen.fillRect(0, 0, Width, Height);
//		gScreen.drawImage(offScreenBuffer, 0, 0, Width, Height, null);
//		gScreen.dispose();
//		
//	}
	
	@Override
	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
		
		gOffscreen = offScreenBuffer.getGraphics(); 
		gOffscreen.setColor(Color.white);
//		gOffscreen.fillRect(0, 0, offScreenBuffer.getWidth(), offScreenBuffer.getHeight());
		gameState.render(gOffscreen);
		gOffscreen.dispose();
		g.fillRect(0, 0, bufferWidth, bufferHeight);
		g.drawImage(offScreenBuffer, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		//which is better? getGraphics() or paintComponent?
		//Answer: getGraphics doesn't work lol. Graphics is always null
//		Graphics g2 = getGraphics();
//		g2.setColor(Color.red);
//		g2.fillRect(0, 0, 200, 200);
//		g2.dispose();
	}	

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("cliek");
	}

	public void mousePressed(MouseEvent e) {
		gameState.mousePressed(e.getButton(), e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		gameState.mouseReleased(e.getButton());
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(key == KeyEvent.VK_ESCAPE)
			System.exit(0);

		keyManager.keyPressed(key);

		gameState.keyPressed(key);
	}	

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		keyManager.keyReleased(key);
		
		gameState.keyReleased(key);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		keyManager.keyReleased(KeyEvent.VK_W);
		keyManager.keyReleased(KeyEvent.VK_A);
		keyManager.keyReleased(KeyEvent.VK_S);
		keyManager.keyReleased(KeyEvent.VK_D);
	}
	
}
