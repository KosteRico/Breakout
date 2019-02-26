package com.kosterico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kosterico.myfirstgame.BreakoutGame;

public class MenuScreen implements Screen {

    private OrthographicCamera camera;

    private Music backgroundMusic;

    private final String str = "Breakout Game";
    private BitmapFont font;
    private GlyphLayout text;


    private Vector2 iconPos;
    private Vector2 statsPos;

    private Sprite iconPlay;
    private Sprite statsIcon;
    private BreakoutGame game;
    private ScreenManager screenManager;

    public MenuScreen(BreakoutGame g, ScreenManager sm) {
        this.game = g;
        this.screenManager = sm;

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("br.mp3"));
        backgroundMusic.setVolume(0.6f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(true, BreakoutGame.GAME_WIDTH, BreakoutGame.GAME_HEIGHT);

        iconPlay = new Sprite( new Texture("icon.png"), 276, 138);
        statsIcon = new Sprite(new Texture("stats.png"), 276, 138);

        iconPos = new Vector2(BreakoutGame.GAME_WIDTH/2 - iconPlay.getWidth()/2, BreakoutGame.GAME_HEIGHT*2/5);
        statsPos = new Vector2(BreakoutGame.GAME_WIDTH/2 - statsIcon.getWidth()/2, iconPos.y + statsIcon.getHeight() + 40);

        font = new BitmapFont(Gdx.files.internal("font_menu.fnt"), Gdx.files.internal("font_menu.png"), true);
        text = new GlyphLayout(font, str);

        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(iconPlay.getTexture(), iconPos.x, iconPos.y);
        game.batch.draw(statsIcon.getTexture(), statsPos.x, statsPos.y);
        font.draw(game.batch, str, BreakoutGame.GAME_WIDTH/2 - text.width/2, BreakoutGame.GAME_HEIGHT*1/5);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.x > iconPos.x && touchPos.x < iconPos.x + iconPlay.getWidth()) {
                if (touchPos.y > iconPos.y && touchPos.y < iconPos.y + iconPlay.getHeight()) {
                    screenManager.set(new PlayScreen(game, screenManager));
                    //game.setScreen(screenManager.getPeek());
                }
            }

            if (touchPos.x > statsPos.x && touchPos.x < statsPos.x + statsIcon.getWidth()) {
                if (touchPos.y > statsPos.y && statsPos.y < statsPos.y + statsIcon.getHeight()) {
                    screenManager.push(new ResultScreen(game, screenManager));
                    //game.setScreen(screenManager.getPeek());
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        font.dispose();
    }
}
