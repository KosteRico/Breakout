package com.kosterico.myfirstgame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kosterico.myfirstgame.BreakoutGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Breakout Game";
        config.width = (int) BreakoutGame.GAME_WIDTH;
        config.height = (int) BreakoutGame.GAME_HEIGHT;
        config.resizable = false;
        config.addIcon("icon_128.png", Files.FileType.Internal);
        config.addIcon("icon_32.png", Files.FileType.Internal);
        config.addIcon("icon_16.png", Files.FileType.Internal);
        new LwjglApplication(new BreakoutGame(), config);
    }
}
