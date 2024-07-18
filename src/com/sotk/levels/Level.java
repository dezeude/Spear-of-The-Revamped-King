package com.sotk.levels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.entities.Creature;
import com.sotk.entities.Enemy;
import com.sotk.entities.Entity;
import com.sotk.entities.Goblin;
import com.sotk.entities.Interactable;
import com.sotk.entities.King;
import com.sotk.entities.NPC;
import com.sotk.entities.Player;
import com.sotk.entities.Projectile;
import com.sotk.entities.Spear;
import com.sotk.main.GamePanel;
import com.sotk.managers.Camera;
import com.sotk.managers.Collisions;
import com.sotk.managers.TileMap;
import com.sotk.states.GameState;

public class Level {
	public static Level curLevel;
	Player p;
	static int[][] map;
	final int TILELENGTH = 32;
	TileMap tileMap;
	Background background;
	String path;

	public GamePanel game;
	GameState gState;

	ArrayList<Enemy> enemies;
	ArrayList<NPC> npcs;
	ArrayList<Projectile> projs;

	ArrayList<Entity> entities;

	Interactable selectedEntity;

	boolean showSpearTrajec = false;

	public Level(String path, GameState gState, GamePanel game) {
		this.path = path;
		this.gState = gState;
		this.game = game;

	}

	public void init() {
		projs = new ArrayList<>();
		enemies = new ArrayList<>();
		npcs = new ArrayList<>();
		tileMap = new TileMap(path, this);
		p = new Player(64, 0, this);
		background = new Background(new Color(126, 146, 176));

		// add the background layers
		BackImage b = new BackImage(path + "layers/Layer8.png", 0.1f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer7.png", 0.2f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer6.png", 0.4f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer5.png", 0.4f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer4.png", 0.4f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer3.png", 0.6f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer2.png", 0.8f, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

		b = new BackImage(path + "layers/Layer1.png", 1, 0, 210, 928, 793 - 210 - 65);
		background.addLayer(b);

//		tileMap.loadTMXMap();
		curLevel = this;
	}

	public void update() {
		p.update();
		if (p.getVelX() > 0 && Camera.getXOffset() > 0)
			background.moveRight();
		else if (p.getVelX() < 0 && Camera.getXOffset() > 0)
			background.moveLeft();

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.canRemove) {
				enemies.remove(i);
				continue;
			}
			e.update();
		}

		for (NPC npc : npcs)
			npc.update();

		for (int i = 0; i < projs.size(); i++) {
			Projectile proj = projs.get(i);
			if (proj.canRemove) {
				projs.remove(i);
				continue;
			}
			proj.update();
		}

	}

	public void render(Graphics g) {
		background.render(g);
		tileMap.render(g);
		p.render(g);
		for (Creature e : enemies)
			e.render(g);
		for (NPC npc : npcs)
			npc.render(g);
		for (Projectile proj : projs)
			proj.render(g);

		if (showSpearTrajec) {
			Vector2i mouseCoords = game.getMouseWindowCoords();
			Vector2f spearVelocity = new Vector2f((mouseCoords.x + Camera.getXOffset()) - p.centerPos().x,
					(mouseCoords.y + Camera.getYOffset()) - p.centerPos().y).normalize(15f);
			Vector2i playerPos = p.centerPos();

			Spear tempSpear = new Spear(playerPos, spearVelocity);

			QuadCurve2D q = new QuadCurve2D.Float();

			// game to window coords
			Vector2i pScreenCoords = new Vector2i(p.centerPos().x - Camera.getXOffset(),
					p.centerPos().y - Camera.getYOffset());

			int maxDisplacement = Math.toIntExact(Math.round(tempSpear.TrajecRange()));

			//anywhere on the curve in game coordinates
			int anywhereOnCurveX = pScreenCoords.x + (maxDisplacement / 2);
			int anywhereOnCurveY = pScreenCoords.y - Math.toIntExact(Math.round(tempSpear.calcYTrajecFromX(maxDisplacement / 2)));

			int cpX = 2 * anywhereOnCurveX - pScreenCoords.x / 2 - (pScreenCoords.x + maxDisplacement) / 2;
			int cpY = 2 * anywhereOnCurveY - pScreenCoords.y / 2 - pScreenCoords.y / 2;

//			mouseCoords.x - pScreenCoords.x, -tempSpear.calcYTrajecFromX(mouseCoords.x - pScreenCoords.x, pScreenCoords.x), // critical point

			// assuming player is aiming to the right
			q.setCurve(pScreenCoords.x, pScreenCoords.y, // starting point
					cpX, cpY, // critical point
					pScreenCoords.x + maxDisplacement, pScreenCoords.y // end point
			);

//			addProjectile(tempSpear);

			// negate y values since graphics displays

			Graphics2D g2d = (Graphics2D) g.create();
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
			g2d.setStroke(dashed);
			g2d.setColor(Color.white);
			g2d.draw(q);
//			g2d.drawLine(pScreenCoords.x, pScreenCoords.y, mouseCoords.x, mouseCoords.y);
		}

	}

	public void mousePressed(int mouseBtn, int x, int y) {
		// left click
		if (mouseBtn == MouseEvent.BUTTON1) {
//			System.out.println("Left Click!");
			p.attack();
		}
		// right click
		if (mouseBtn == MouseEvent.BUTTON3) {
			p.holdSpear();
			showSpearTrajec = true;
		}

	}

	public void mouseReleased(int mouseBtn, int x, int y) {
		// right click
		if (mouseBtn == MouseEvent.BUTTON3) {
			Vector2i point = game.windowToBufferPoint(new Vector2i(x, y));
			p.setThrowingState(point.x, point.y);
			showSpearTrajec = false;
		}
	}

	public void addMob(String name, int x, int y, String[] metadata) {
		switch (name) {
		case "Goblin":
			Goblin goblin = new Goblin(x, y, this);
			goblin.addMetaData(metadata);
			enemies.add(goblin);
			break;

		case "King":
			King king = new King(x, y, this);
			king.setSpeech(metadata);
			npcs.add(king);
			break;
		}
	}

	public void addProjectile(Projectile proj) {
		projs.add(proj);
	}

	public static void setMap(int[][] wmap) {
		map = wmap;
		Collisions.setMap(map);
	}

	public static int[][] getMap() {
		return map;
	}

	public Rectangle getPlayerBounds() {
		return p.getBounds();
	}

	// returns true if the entity was damaged/attacked.
	public boolean damageEnemies(Rectangle bounds, int damage) {
		for (Creature e : enemies) {
			if (bounds.intersects(e.getBounds())) {
				return e.damage(damage);
			}
		}
		return false;
	}

	public boolean enemyAttack(Rectangle bounds, int damage) {
		if (bounds.intersects(p.getBounds())) {
			p.damage(damage);
			Vector2i boundsCenter = new Vector2i(bounds.x + bounds.width / 2, bounds.y + bounds.height);
			Vector2i dest = new Vector2i();
			p.centerPos().sub(boundsCenter, dest);
			p.addForce(dest);
			return true;
		}
		return false;

	}

	public void interact() {
		if (selectedEntity != null)
			selectedEntity.interact();
	}

	public void selectEntity(Interactable entity) {
		selectedEntity = entity;
	}

	public Player getPlayer() {
		return p;
	}

}
