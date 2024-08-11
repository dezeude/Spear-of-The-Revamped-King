package com.sotk.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.sotk.levels.Level;
import com.sotk.main.GamePanel;
import com.sotk.main.Launcher;

public class GameState extends State {
    GamePanel game;
    volatile Level curLevel;
    //	Level nextLevel;
    Thread nLevelLoader;
    public float count = 0f;

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
        if (nLevelLoader != null && nLevelLoader.isAlive()) {
            // if the thread is loading the next level
            count += 0.025f;
            if (count >= 3.5f)
                count = 0f;
        } else
            curLevel.update();
    }

    @Override
    public void render(Graphics g) {
        if (nLevelLoader != null && nLevelLoader.isAlive()) {
            // if the thread is loading the next level
            String[] dots = {"", ".", "..", "..."};
            int width = game.getGraphicsWidth();
            int height = game.getGraphicsHeight();
            Graphics gc = g.create();
            gc.clearRect(0, 0, width, height);
            gc.setColor(Color.ORANGE);
            gc.setFont(new Font("Algerian", Font.BOLD, 30));
            gc.drawString("Loading" + dots[(int) Math.round(count)], width / 2 - 90, (int) Math.round(height * 0.6));
            gc.drawImage(Launcher.icon, width / 2 - Launcher.icon.getWidth(), height / 2 - Launcher.icon.getHeight(), null);
        } else // render the level
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
        this.curLevel = new Level("/levels/" + newLevel + "/", this, game);
        Level.curLevel = this.curLevel;
    }

    public void loadLevel(String nextLevelStr) {
//		this.nextLevel = new Level("/levels/" + nextLevelStr + "/", this, game);
        GameState gs = this;
        nLevelLoader = new Thread() {
            String path = "/levels/" + nextLevelStr + "/";
            GameState gState = gs;
            GamePanel gameP = game;

            @Override
            public void run() {
                Level l = new Level(path, gState, gameP);
                gState.setLevelAsync(l);
                System.out.println("Next Level Loaded!");
                gState.count = 0;
            }
        };

        nLevelLoader.start();
    }

    public void setLevelAsync(Level level) {
        this.curLevel = level;
        this.nLevelLoader = null;
    }

}
