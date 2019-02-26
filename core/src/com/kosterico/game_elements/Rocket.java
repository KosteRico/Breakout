package com.kosterico.game_elements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.kosterico.myfirstgame.BreakoutGame;

public class Rocket implements Collidable{

    private Vector3 position;

    public static final float WIDTH = 190 / 2;
    public static final float HEIGHT = 15 / 2;
    private static final float SPEED = 300;

    private Rectangle model;

    private Rocket() {
        position = new Vector3();
        model = new Rectangle();
    }

    public static Rocket createRocket() {
        Rocket rocket = new Rocket();
        rocket.position.x = BreakoutGame.GAME_WIDTH / 2 - WIDTH / 2;
        rocket.position.y = BreakoutGame.GAME_HEIGHT - Borderline.PADDING - HEIGHT;
        rocket.position.z = 0;
        rocket.model.setPosition(rocket.position.x, rocket.position.y);
        rocket.model.setSize(WIDTH, HEIGHT);
        return rocket;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void moveLeft(float deltaTime) {
        position.x -= SPEED * deltaTime;
        if (position.x <= Borderline.PADDING) {
            position.x = Borderline.PADDING;
        }
        model.x = position.x;
    }

    public void moveRight(float deltaTime) {
        position.x += SPEED * deltaTime;
        if ((position.x + WIDTH) >= (BreakoutGame.GAME_WIDTH - Borderline.PADDING)) {
            position.x = BreakoutGame.GAME_WIDTH - WIDTH - Borderline.PADDING;
        }
        model.x = position.x;
    }

    @Override
    public int getCollideableID() {
        return ID_COLLIDEABLE_ROCKET;
    }

    @Override
    public Rectangle getMathModel() {
        return model;
    }

}
