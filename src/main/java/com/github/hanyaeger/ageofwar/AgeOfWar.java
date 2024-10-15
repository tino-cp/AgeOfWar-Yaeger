package com.github.hanyaeger.ageofwar;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.ageofwar.entities.scenes.Scene;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;

public class AgeOfWar extends YaegerGame {
    private static final int GAME_WIDTH = 1550;
    private static final int GAME_HEIGHT = 500;
    private static final String GAME_TITLE = "Age Of War";

    private static final String BACKGROUND_AUDIO = "audio/ageofwar-song.mp3";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setSize(new Size(GAME_WIDTH, GAME_HEIGHT));
        setGameTitle(GAME_TITLE);
        setBackgroundAudio(BACKGROUND_AUDIO);
    }

    public static int getGameWidth() {
        return GAME_WIDTH;
    }

    public static int getGameHeight() {
        return GAME_HEIGHT;
    }

    @Override
    public void setupScenes() {
        addScene(0, new Scene(this, GAME_TITLE));
        addScene(1, new MainScene(this));
        addScene(2, new Scene(this, "Defeat!"));
        addScene(3, new Scene(this, "Victory!"));
    }
}

