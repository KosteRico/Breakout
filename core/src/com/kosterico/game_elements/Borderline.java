package com.kosterico.game_elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.kosterico.myfirstgame.BreakoutGame;

public class Borderline implements Collidable {

    public static final int PADDING = 20;
    public static final int PADDING_TOP = 60;
    public static final int BORDER_WIDTH = 5;

    private Rectangle model;

    public Borderline(String direction) {
        model = new Rectangle();
        if (direction.equals("left")) {
            model.set(PADDING - BORDER_WIDTH, PADDING_TOP - BORDER_WIDTH, BORDER_WIDTH, BreakoutGame.GAME_HEIGHT - PADDING - PADDING_TOP - BORDER_WIDTH);
        } else if (direction.equals("right")) {
            model.set(BreakoutGame.GAME_WIDTH - PADDING, PADDING_TOP - BORDER_WIDTH, BORDER_WIDTH, BreakoutGame.GAME_HEIGHT - PADDING - PADDING_TOP - BORDER_WIDTH);
        } else if (direction.equals("top")) {
            model.set(PADDING - BORDER_WIDTH, PADDING_TOP - BORDER_WIDTH, BreakoutGame.GAME_WIDTH - PADDING * 2, BORDER_WIDTH);
        }
    }

    public void drawBorderline(ShapeRenderer shapeRenderer) {
        Color color = new Color(Color.WHITE);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(model.x, model.y, model.width, model.height);
    }

    @Override
    public int getCollideableID() {
        return ID_COLLIDEABLE_BORDERLINES;
    }

    @Override
    public Rectangle getMathModel() {
        return model;
    }

}
