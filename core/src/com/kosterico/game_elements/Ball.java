package com.kosterico.game_elements;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kosterico.myfirstgame.BreakoutGame;

import java.util.Random;

public class Ball {
    private Vector3 position;
    private byte xSign;
    private byte ySign;

    private byte bounceCounter;

    private boolean isTangentUsed;

    public static final float DIAMETER = 128 / 9;
    private static float speed;

    private static final byte COLLISION_BOTTOM = 1;//0001
    private static final byte COLLISION_RIGHT = 2;//0010
    private static final byte COLLISION_TOP = 4;//0100
    private static final byte COLLISION_LEFT = 8;//1000

    private static final byte COLLISION_LEFT_BOTTOM = 9;//1001
    private static final byte COLLISION_LEFT_TOP = 12;//1100
    private static final byte COLLISION_RIGHT_BOTTOM = 3;//0011
    private static final byte COLLISION_RIGHT_TOP = 6;//0110

    private float tangent;
    private Circle ballModel;

    private Ball() {
        speed = 200;
        bounceCounter = 0;
        isTangentUsed = false;
        tangent = 1;
        ballModel = new Circle(0, 0, DIAMETER / 2);
        position = new Vector3(0, 0, 0);
        defaultPosition(Rocket.createRocket());
        ySign = -1;
        xSign = 1;
    }

    private void updatePos(float x, float y, float deltaTime) {
        if (!isTangentUsed) {
            position.add(x * deltaTime + xSign * 2.5f, y * tangent * deltaTime + ySign * 2.5f, 0);
            isTangentUsed = true;
        } else {
            position.add(x * deltaTime, y * deltaTime, 0);
        }
        ballModel.setPosition(position.x + DIAMETER / 2, position.y + DIAMETER / 2);
    }

    public void defaultPosition(Rocket rocket) {
        position.set(0, 0, 0);
        ySign = -1;
        xSign = 1;
        bounceCounter = 0;
        updatePos(rocket.getX() + Rocket.WIDTH / 2 - Ball.DIAMETER / 2, BreakoutGame.GAME_HEIGHT - Borderline.PADDING - Rocket.HEIGHT - DIAMETER, 1);
    }

    public void defaultSpeed () {
        speed = 200;
    }

    public static Ball create() {
        Ball ball = new Ball();
        return ball;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void move(float deltaTime) {
        updatePos(xSign * speed, ySign * speed, deltaTime);
    }

    private void randomBounce() throws RuntimeException {
        Random randomSign = new Random();
        int randNum = (int) (Math.random() * 3 + 1);
        boolean randSign = randomSign.nextBoolean();
        float num;
        switch (randNum) {
            case 1:
                num = 3f;
                break;
            case 2:
                num = 1f;
                break;
            case 3:
                num = 0.75f;
                break;
            default:
                throw new RuntimeException("Current position: (" + position.x + "; " + position.y + " )");
        }
        xSign = (byte) (randSign ? -1 : 1);
        isTangentUsed = false;
        tangent /= num;
    }

    public void collisionHandler(Collidable obstacle) {
        int collisionType = isTouched(obstacle);
        if (collisionType < 0) {
            return;
        }

        if (obstacle.getCollideableID() == Collidable.ID_COLLIDEABLE_ROCKET) {
            randomBounce();
            ySign = -1;
            if (collisionType == COLLISION_RIGHT_TOP) {
                xSign = 1;
            } else if (collisionType == COLLISION_LEFT_TOP) {
                xSign = -1;
            }
            return;
        }

        changeSign(collisionType);
    }

    private void changeSign (int collisionType) {

        if (collisionType == COLLISION_LEFT) {
            xSign = -1;
        } else if (collisionType == COLLISION_RIGHT) {
            xSign = 1;
        } else if (collisionType == COLLISION_TOP) {
            ySign = -1;
        } else if (collisionType == COLLISION_BOTTOM) {
            ySign = 1;
        } else if (collisionType == COLLISION_LEFT_TOP || collisionType == COLLISION_RIGHT_TOP || collisionType == COLLISION_LEFT_BOTTOM || collisionType == COLLISION_RIGHT_BOTTOM) {
            xSign = (byte) (-xSign);
            ySign = (byte) (-ySign);
        }
    }

    public void collisionHandler (Brick brick) {
        changeSign(isTouched(brick));

        updateSpeed(brick);
    }

    private void updateSpeed (Brick brick) {
        bounceCounter++;

        if (speed >= 400)
            return;

        if (brick.isRED()) {
            speed += 25;
            return;
        }

        if (bounceCounter == 4) {
            speed += 25;
            return;
        }

        if (bounceCounter == 12) {
            speed += 25;
        }


    }

    public boolean collides(Rectangle obstacle) {
        Vector2 obsCenter = new Vector2(obstacle.x + obstacle.width / 2, obstacle.y + obstacle.height / 2);
        float distanceY = Math.abs(ballModel.y - obsCenter.y);

        if (distanceY > (obstacle.height / 2 + DIAMETER / 2)) {
            return false;
        }

        float distanceX = Math.abs(ballModel.x - obsCenter.x);
        if (distanceX > (obstacle.width / 2 + DIAMETER / 2)) {
            return false;
        }

        if (distanceX <= obstacle.width / 2) {
            return true;
        }

        if (distanceY <= obstacle.height / 2) {
            return true;
        }

        float a = distanceX - obstacle.width / 2;
        float b = distanceY - obstacle.height / 2;
        float cSqr = a * a + b * b;
        return (cSqr <= (DIAMETER/2) * (DIAMETER / 2));
    }

    private int isTouched(Collidable obstacle) {

        byte toLeftCode = 0;
        byte toRightCode = 0;
        byte toTopCode = 0;
        byte toBottomCode = 0;

        if (ballModel.x <= obstacle.getMathModel().x) {
            toLeftCode = COLLISION_LEFT;
            if (ballModel.y >= obstacle.getMathModel().y + obstacle.getMathModel().height) {
                toBottomCode = COLLISION_BOTTOM;
            } else if (ballModel.y <= obstacle.getMathModel().y) {
                toTopCode = COLLISION_TOP;
            }
        } else if (ballModel.x >= obstacle.getMathModel().x + obstacle.getMathModel().width) {
            toRightCode = COLLISION_RIGHT;
            if (ballModel.y >= obstacle.getMathModel().y + obstacle.getMathModel().height) {
                toBottomCode = COLLISION_BOTTOM;
            } else if (ballModel.y <= obstacle.getMathModel().y) {
                toTopCode = COLLISION_TOP;
            }
        } else {
            if (ballModel.y >= obstacle.getMathModel().y + obstacle.getMathModel().height) {
                toBottomCode = COLLISION_BOTTOM;
            } else if (ballModel.y <= obstacle.getMathModel().y) {
                toTopCode = COLLISION_TOP;
            }
        }

        return (toBottomCode + toLeftCode + toRightCode + toTopCode);
    }

    public Circle getBallModel() {
        return ballModel;
    }

}