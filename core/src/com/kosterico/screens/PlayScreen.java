package com.kosterico.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;
import com.badlogic.gdx.utils.Array;
import com.kosterico.game_elements.Ball;
import com.kosterico.game_elements.Borderline;
import com.kosterico.game_elements.Brick;
import com.kosterico.game_elements.HealthPoints;
import com.kosterico.game_elements.Rocket;
import com.kosterico.game_elements.SavingData;
import com.kosterico.myfirstgame.BreakoutGame;

import java.util.Date;

public class PlayScreen implements Screen {

    public static final int MAX_SCORE = 256;

    private ScreenManager screenManager;

    private Texture brickRed;
    private Texture brickBrown;
    private Texture brickGreen;
    private Texture brickBlue;
    private Texture rocketTexture;
    private Texture ballTexture;
    private Texture HPTexture;

    private Music backgroundMusic;
    private Sound bounceSound;

    private BitmapFont font;

    private BreakoutGame game;
    private Rocket rocket;
    private Ball ball;
    private Borderline borderlineLeft;
    private Borderline borderlineRight;
    private Borderline borderlineTop;
    private Array<Array<Brick>> bricks;
    private OrthographicCamera camera;

    private int index;
    private float timeCounter;
    private boolean isGameStarted;
    private int score = 0;
    private boolean isBrickCrashed;

    public PlayScreen(BreakoutGame game, ScreenManager sm) {
        this.screenManager = sm;
        isBrickCrashed = false;
        camera = new OrthographicCamera();
        timeCounter = 0f;
        index = 0;

        camera.setToOrtho(true, BreakoutGame.GAME_WIDTH, BreakoutGame.GAME_HEIGHT);
        bricks = new Array<Array<Brick>>(8);
        Brick.fillField(bricks);
        this.game = game;
        font = new BitmapFont(Gdx.files.internal("font_text.fnt"), Gdx.files.internal("font_text.png"), true);

        bounceSound = Gdx.audio.newSound(Gdx.files.internal("bounce.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("du_hast.mp3"));
        backgroundMusic.setVolume(0.35f);
        backgroundMusic.setLooping(true);

        ballTexture = new Texture("ball.png");
        brickRed = new Texture("red_brick.png");
        brickBrown = new Texture("brown_brick.png");
        brickGreen = new Texture("green_brick.png");
        brickBlue = new Texture("blue_brick.png");
        rocketTexture = new Texture("rocket.png");
        HPTexture = new Texture("HealthPoints.png");

        rocket = Rocket.createRocket();
        HealthPoints.create();
        ball = Ball.create();
        borderlineLeft = new Borderline("left");
        borderlineRight = new Borderline("right");
        borderlineTop = new Borderline("top");
        isGameStarted = false;
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeType.Filled);
        borderlineTop.drawBorderline(game.shapeRenderer);
        borderlineRight.drawBorderline(game.shapeRenderer);
        borderlineLeft.drawBorderline(game.shapeRenderer);
        game.shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        HealthPoints.drawHP(game.batch, HPTexture);
        game.batch.draw(rocketTexture, rocket.getX(), rocket.getY(), Rocket.WIDTH, Rocket.HEIGHT);
        game.batch.draw(ballTexture, ball.getX(), ball.getY(), Ball.DIAMETER, Ball.DIAMETER);
        drawBricks();
        font.draw(game.batch, "Score: " + score, 30, 30);
        if (!isGameStarted) {
            String[] strings = new String[]{"3", "2", "1", "Go"};
            font.draw(game.batch, strings[index], BreakoutGame.GAME_WIDTH/2, BreakoutGame.GAME_HEIGHT/2);
            if (timeCounter >= 1f) {
                timeCounter = 0f;
                index++;
                if (index == 4) {
                    isGameStarted = true;
                    backgroundMusic.play();
                }
            } else {
                timeCounter += Gdx.graphics.getDeltaTime();
            }
        }
        game.batch.end();

        if (isGameStarted) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
//            isGameStarted = ball.startGame(isGameStarted, Gdx.graphics.getDeltaTime());
                rocket.moveLeft(Gdx.graphics.getDeltaTime());
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
//            isGameStarted = ball.startGame(isGameStarted, Gdx.graphics.getDeltaTime());
                rocket.moveRight(Gdx.graphics.getDeltaTime());
            }

            if (ball.collides(rocket.getMathModel()))
                ball.collisionHandler(rocket);

            for (int i = 0; i < 8; i++) {
                for (int j = 7; j >= 0; j--) {
                    Brick brick = bricks.get(i).get(j);
                    if (brick.isTouched()) {
                        continue;
                    }
                    if (ball.collides(brick.getMathModel())) {
                        if (!isBrickCrashed) {
                            isBrickCrashed = true;
                        }
                        score += brick.addPoints();
                        ball.collisionHandler(brick);
                        brick.setTouched();
                        bounceSound.play(0.75f);

                    }
                }
            }

            if (ball.collides(borderlineLeft.getMathModel()))
                ball.collisionHandler(borderlineLeft);

            if (ball.collides(borderlineRight.getMathModel()))
                ball.collisionHandler(borderlineRight);

            if (ball.collides(borderlineTop.getMathModel()))
                ball.collisionHandler(borderlineTop);

            ball.move(Gdx.graphics.getDeltaTime());

        }


        if (ball.getY() >= BreakoutGame.GAME_HEIGHT) {
            HealthPoints.dec();
            if (!HealthPoints.isZero()) {
                ball.defaultPosition(rocket);
                ball.defaultSpeed();
            } else {
                SavingData.loadData();
                SavingData.saveDatas(new Date(), score);
                backgroundMusic.stop();
                HealthPoints.defaultHP();
                screenManager.set(new MenuScreen(game, screenManager));
                //game.setScreen(screenManager.getPeek());
            }
        }

        if (ball.getX() + Ball.DIAMETER <= 0 || ball.getX() >= BreakoutGame.GAME_WIDTH || ball.getY() <= 0) {
            ball.defaultPosition(rocket);
        }

        if (score > 0 && score % MAX_SCORE == 0 && isBrickCrashed) {
            isBrickCrashed = false;
            ball.defaultPosition(rocket);
            for (Array<Brick> b : bricks) {
                for (Brick br : b) {
                    br.setUntouched();
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
        brickRed.dispose();
        brickBlue.dispose();
        brickGreen.dispose();
        brickBrown.dispose();
        rocketTexture.dispose();
        ballTexture.dispose();
    }

    private void drawBricks() {
        for (int i = 0; i < bricks.size; i++) {
            for (int j = 0; j < bricks.get(i).size; j++) {
                Brick brick = bricks.get(i).get(j);
                if (brick.isTouched())
                    continue;
                if (brick.isRED()) {
                    game.batch.draw(brickRed, brick.getX(), brick.getY(), Brick.WIDTH, Brick.HEIGHT);
                } else if (brick.isBROWN()) {
                    game.batch.draw(brickBrown, brick.getX(), brick.getY(), Brick.WIDTH, Brick.HEIGHT);
                } else if (brick.isGREEN()) {
                    game.batch.draw(brickGreen, brick.getX(), brick.getY(), Brick.WIDTH, Brick.HEIGHT);
                } else {
                    game.batch.draw(brickBlue, brick.getX(), brick.getY(), Brick.WIDTH, Brick.HEIGHT);
                }
            }
        }
    }

}
