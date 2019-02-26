package com.kosterico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kosterico.game_elements.SavingData;
import com.kosterico.myfirstgame.BreakoutGame;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class ResultScreen implements Screen {

    private OrthographicCamera camera;

    private Sprite backIcon;

    private Vector2 backPos;

    private BitmapFont font;

    private BreakoutGame game;

    private ScreenManager screenManager;

    public ResultScreen(BreakoutGame game, ScreenManager screenManager) {
        this.game = game;
        this.screenManager = screenManager;

        SavingData.loadData();

        camera = new OrthographicCamera();
        camera.setToOrtho(true, BreakoutGame.GAME_WIDTH, BreakoutGame.GAME_HEIGHT);

        backIcon = new Sprite(new Texture("back.png"), 176, 86);

        backPos = new Vector2(5, 5);

        font = new BitmapFont(Gdx.files.internal("font_text.fnt"), Gdx.files.internal("font_text.png"), true);

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
        game.batch.draw(backIcon.getTexture(), backPos.x, backPos.y);
        printResults(game.batch);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (touchPos.x > backPos.x && touchPos.x < backPos.x + backIcon.getWidth())
                if (touchPos.y > backPos.y && touchPos.y < backPos.y + backIcon.getHeight()) {
                    screenManager.pop();
                    //game.setScreen(screenManager.getPeek());
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
        font.dispose();
    }

    private void printResults(SpriteBatch batch) {

        font.draw(batch, "Date:", 200, 180);
        font.draw(batch, "Score: ", 450, 180);

        if (!SavingData.getDates().isEmpty()) {
            Iterator<Map.Entry<Date, Integer>> i = SavingData.getDates().entrySet().iterator();

            int j = 0;
            while (i.hasNext()) {
                Map.Entry<Date, Integer> it = i.next();
                font.draw(batch, "#" + Integer.toString(j + 1), 125, 215 + 35*j);
                font.draw(batch, handleDate(it.getKey()), 200, 215 + 35 * j);
                font.draw(batch, it.getValue().toString(), 450, 215 + 35 * j);
                j++;
            }
        }
    }

    private String handleDate (Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return (c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + "." + c.get(Calendar.YEAR));
    }

}
