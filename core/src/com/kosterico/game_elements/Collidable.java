package com.kosterico.game_elements;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    int ID_COLLIDEABLE_BRICK = 1111;
    int ID_COLLIDEABLE_BORDERLINES = 1110;
    int ID_COLLIDEABLE_ROCKET = 1;
    int getCollideableID();
    Rectangle getMathModel();
}
