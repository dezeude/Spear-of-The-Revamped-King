package com.sotk.levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.entities.Creature;
import com.sotk.entities.Enemy;
import com.sotk.entities.Goblin;
import com.sotk.entities.Interactable;
import com.sotk.entities.King;
import com.sotk.entities.NPC;
import com.sotk.entities.Player;
import com.sotk.entities.Projectile;
import com.sotk.main.GamePanel;
import com.sotk.managers.Camera;
import com.sotk.managers.Collisions;
import com.sotk.managers.TileMap;
import com.sotk.states.GameState;
import com.sotk.states.playerstates.PlayerState;

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
//	ArrayList<Projectiles> projectiles;

	public Vector2f gravity = new Vector2f(0, .981f / 2);

	Interactable selectedEntity;

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

		for (Projectile proj : projs) {

			Vector2f temp = new Vector2f();
			gravity.mul(proj.getMass(), temp);
			proj.applyForce(temp);
			proj.update();
//			System.out.println("Update Projs!");
		}

//		projs.forEach((n) -> n.update());

//		checkCollisions();
//		System.out.println(enemies.size());
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

//		draw the attackBounds
//		Rectangle b = playerAttackBounds;
//		if(b != null) {
//			g.fillRect(b.x - Camera.getXOffset(), b.y - Camera.getYOffset(), b.width, b.height);
//			System.out.println(b.x);
//
//		}

	}

	public void mousePressed(int mouseBtn, int x, int y) {
		// left click
		if (mouseBtn == MouseEvent.BUTTON1) {
//			System.out.println("Left Click!");
			p.attack();
		}
		// right click
		if (mouseBtn == MouseEvent.BUTTON3) {
//			System.out.println("Right Click!");
			Vector2i point = game.windowToBufferPoint(new Vector2i(x, y));
//			p.throwSpear(x, y);
			p.throwSpear(point);
			p.setState(PlayerState.throwing);
		}

	}

	public void mouseReleased(int mouseBtn) {

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

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void damageEnemies(Rectangle bounds, int damage) {
		for (Creature e : enemies) {
			if (bounds.intersects(e.getBounds())) {
				e.damage(damage);
			}
		}
	}

	public void enemyAttack(Rectangle bounds, int damage) {
		if (bounds.intersects(p.getBounds())) {
			p.damage(damage);
			Vector2i boundsCenter = new Vector2i(bounds.x + bounds.width / 2, bounds.y + bounds.height);
			Vector2i dest = new Vector2i();
			p.centerPos().sub(boundsCenter, dest);
			p.addForce(dest);
		}

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
