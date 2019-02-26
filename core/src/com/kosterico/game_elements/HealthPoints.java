package com.kosterico.game_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.kosterico.myfirstgame.BreakoutGame;

public class HealthPoints {

    private static final float WIDTH = 128 / 5;
    private static final float HEIGHT = 116 / 5;

    private static byte counter;
    private static Vector2 locationOfFirst;

    public static void create() {
        counter = 3;
        locationOfFirst = new Vector2(BreakoutGame.GAME_WIDTH - 100, 20);
    }

    public static boolean isZero() {
        return (counter == 0);
    }

    public static void drawHP (SpriteBatch batch, Texture img) {
        int j = 0;
        for (int i = 0; i < counter; i++) {
            batch.draw(img, locationOfFirst.x + j, locationOfFirst.y, WIDTH, HEIGHT);
            j += 28;
        }
    }

    public static void defaultHP () {
        counter = 3;
    }

    public static void dec() {
        counter--;
    }

}
