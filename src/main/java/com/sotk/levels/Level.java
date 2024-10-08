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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.sotk.entities.Creature;
import com.sotk.entities.Enemy;
import com.sotk.entities.Entity;
import com.sotk.entities.Goblin;
import com.sotk.entities.Interactable;
import com.sotk.entities.King;
import com.sotk.entities.Mushroom;
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

    public class Door {
        private final Rectangle bounds;
        private final String levelTo;

        // bounds, level-file-name
        public Door(Rectangle bounds, String levelTo) {
            this.bounds = bounds;
            this.levelTo = levelTo;
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public String getlevelTo() {
            return levelTo;
        }
    }

    ArrayList<Door> doors;

    public Level(String path, GameState gState, GamePanel game) {
        this.path = path;
        this.gState = gState;
        this.game = game;
        init();
    }

    public void init() {
        projs = new ArrayList<>();
        enemies = new ArrayList<>();
        npcs = new ArrayList<>();
        doors = new ArrayList<>();
        background = new Background(new Color(126, 146, 176));
        tileMap = new TileMap(path, this);
        curLevel = this;
    }

    public void update() {
        p.update();
        if (p.canRemove) {
            //change gamestate
            //TODO: make gamestates use the same FSM as CreatureState and PlayerState
        }

        // loop through doors
        for (Door door : doors) {
            if (p.getBounds().intersects(door.getBounds())) {
                System.out.println("Changing Levels!");
                //change level
                gState.loadLevel(door.getlevelTo());
                return;
            }
        }

        //move background
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

        for (Door door : doors) {
            Rectangle bounds = door.getBounds();
            Graphics g2 = (Graphics) g.create();
            g2.setColor(Color.orange);
            g2.fillRect(bounds.x - Camera.getXOffset(), bounds.y - Camera.getYOffset(), (int) bounds.getWidth(), (int) bounds.getHeight());
        }

        if (showSpearTrajec) {
            Vector2i mouseCoords = new Vector2i(game.getMouseWindowCoords().x, game.getMouseWindowCoords().y);
            mouseCoords = game.windowToBufferPoint(mouseCoords);
            Vector2i playerPos = new Vector2i(p.centerPos().x - Camera.getXOffset(),
                    p.centerPos().y - Camera.getYOffset());

            Vector2f spearVelocity = new Vector2f(mouseCoords.x - playerPos.x, mouseCoords.y - playerPos.y)
                    .normalize(15f);

            // game to window coords
            Vector2i pScreenCoords = new Vector2i(p.centerPos().x - Camera.getXOffset(),
                    p.centerPos().y - Camera.getYOffset());

            Graphics2D g2d = (Graphics2D) g.create();
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2d.setStroke(dashed);
            g2d.setColor(Color.white);

            if (mouseCoords.y > pScreenCoords.y) {
                g2d.drawLine(pScreenCoords.x, pScreenCoords.y, mouseCoords.x, mouseCoords.y);

                return;
            }

            Spear tempSpear = new Spear(playerPos, spearVelocity, p);

            QuadCurve2D q = new QuadCurve2D.Float();

            int maxDisplacement = Math.toIntExact(Math.round(tempSpear.TrajecRange()));

            // anywhere on the curve in game coordinates
            int anywhereOnCurveX = pScreenCoords.x + (maxDisplacement / 2);
            int anywhereOnCurveY = pScreenCoords.y
                    - Math.toIntExact(Math.round(tempSpear.calcYTrajecFromX(maxDisplacement / 2)));

            int cpX = 2 * anywhereOnCurveX - pScreenCoords.x / 2 - (pScreenCoords.x + maxDisplacement) / 2;
            int cpY = 2 * anywhereOnCurveY - pScreenCoords.y / 2 - pScreenCoords.y / 2;

            // draw spear predictor
            q.setCurve(pScreenCoords.x, pScreenCoords.y, // starting point
                    cpX, cpY, // critical point
                    pScreenCoords.x + maxDisplacement, pScreenCoords.y // end point
            );

//			addProjectile(tempSpear);

            g2d.draw(q);
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
            case "Player":
                this.p = new Player(x, y, this);
                Camera.setPos(x, y);
                break;
            case "Goblin":
                Goblin goblin = new Goblin(x, y);
                goblin.addMetaData(metadata);
                enemies.add(goblin);
                break;

            case "King":
                King king = new King(x, y, this);
                king.setSpeech(metadata);
                npcs.add(king);
                break;

            case "Mushroom":
                System.out.println("mushroom added");
                Mushroom mushroom = new Mushroom(x, y);
                mushroom.addMetaData(metadata);
                enemies.add(mushroom);
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

    /**
     * returns true if the entity was damaged/attacked.
     */
    public boolean damageEnemies(Rectangle bounds, int damage, Creature owner) {
        // TODO: store the owner of the attack bounds for better directional knockback
        for (Creature e : enemies) {
            if (bounds.intersects(e.getBounds())) {
                Vector2i dir = new Vector2i();
                e.centerPos().sub(owner.centerPos().x, owner.centerPos().y(), dir);

                return e.damage(damage, new Vector2f(dir.x, dir.y));
            }
        }
        return false;
    }

    public boolean enemyAttack(Rectangle bounds, int damage, Creature owner) {
        if (bounds.intersects(p.getBounds())) {
            Vector2i dir = new Vector2i();
            p.centerPos().sub(owner.centerPos().x, owner.centerPos().y(), dir);

            return p.damage(damage, new Vector2f(dir.x, dir.y));
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

    public void addDoor(Rectangle bounds, String name) {
        doors.add(new Door(bounds, name));
    }

    public String getPath() {
        return this.path;
    }

    public Background getBackground() {
        return this.background;
    }

}
