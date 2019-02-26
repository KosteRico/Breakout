package com.kosterico.game_elements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Brick implements Collidable {
    private int ID;
    private Vector3 position;
    private Rectangle model;
    private boolean isTouched;

    public static final float WIDTH = 384 / 5;
    public static final float HEIGHT = 128 / 5;
    private static final byte ID_RED = 0;
    private static final byte ID_BROWN = 1;
    private static final byte ID_GREEN = 2;
    private static final byte ID_BLUE = 3;

    private Brick(int id) {
        this.ID = id;
        isTouched = false;
        position = new Vector3();
        model = new Rectangle();
    }

    public boolean isRED() {
        return ID == ID_RED;
    }

    public boolean isBROWN() {
        return ID == ID_BROWN;
    }

    public boolean isGREEN() {
        return ID == ID_GREEN;
    }

    public boolean isBLUE() {
        return ID == ID_BLUE;
    }


    private void setPosition(float x, float y) {
        position.set(x, y, 0);
        model.set(x, y, WIDTH, HEIGHT);
    }

    public int addPoints() {
        if (isRED()){
            return 7;
        }

        if (isBROWN()) {
            return 5;
        }

        if (isGREEN()) {
            return 3;
        }

        if (isBLUE()) {
            return 1;
        }

        throw new IllegalArgumentException("Incorrect ID");
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public static void fillField(Array<Array<Brick>> bricks) {
        setColumns(bricks);
        for (int j = 0; j < 8; j++) {
            Array<Brick> brickCol = new Array<Brick>();
            for (int i = 0; i < 8; i++) {
                Brick brick = new Brick(i / 2);
                brick.setPosition(Borderline.PADDING + (Brick.WIDTH + 5) * j, Borderline.PADDING_TOP + (Brick.HEIGHT + 5) * i + 5);
                brickCol.add(brick);
            }
            bricks.add(brickCol);
        }
    }

    private static void setColumns(Array<Array<Brick>> bricks) {
        for (Array<Brick> brickCol : bricks) {
            brickCol = new Array<Brick>(8);
        }
    }

    public void setTouched () {
        isTouched = true;
    }

    public void setUntouched () {
        isTouched = false;
    }

    public boolean isTouched() {
        return isTouched;
    }

    @Override
    public int getCollideableID() {
        return ID_COLLIDEABLE_BRICK;
    }

    @Override
    public Rectangle getMathModel() {
        return model;
    }

}
