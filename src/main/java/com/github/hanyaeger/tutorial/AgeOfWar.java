package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;

public class AgeOfWar extends YaegerGame {
    private static final int WIDTH = 1550;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setSize(new Size(WIDTH, HEIGHT));
        setGameTitle("Age Of War");
        //setBackgroundAudio("audio/ageofwar-song.mp3");
    }

    @Override
    public void setupScenes() {
        addScene(0, new Scene(this, "Age Of War"));
        addScene(1, new MainScene(this));
        addScene(2, new Scene(this, "Defeat!"));
        addScene(3, new Scene(this, "Victory!"));
    }
}

