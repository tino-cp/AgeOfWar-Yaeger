package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;

public class AgeOfWar extends YaegerGame {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setGameTitle("Age Of War");
        setSize(new Size(1550, 500));
    }

    @Override
    public void setupScenes() {
        addScene(1, new MainScene(this));
    }
}

