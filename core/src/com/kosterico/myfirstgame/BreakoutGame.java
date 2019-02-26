package com.kosterico.myfirstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kosterico.game_elements.Borderline;
import com.kosterico.game_elements.Brick;
import com.kosterico.screens.MenuScreen;
import com.kosterico.screens.PlayScreen;
import com.kosterico.screens.ScreenManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BreakoutGame extends Game {

    public static final float GAME_WIDTH = (((int) Brick.WIDTH + 5) * 8 + Borderline.PADDING * 2);
    public static final float GAME_HEIGHT = 680;
    private ScreenManager sm;

    private Logger log = Logger.getLogger(Game.class.getName());
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        log.setLevel(Level.INFO);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        sm = new ScreenManager();
        sm.push(new MenuScreen(this, sm));
    }

    @Override
    public void render() {
        super.render();
        setScreen(sm.getPeek());
    }

    @Override
    public void dispose() {
        log.info("Disposed");
    }

}
