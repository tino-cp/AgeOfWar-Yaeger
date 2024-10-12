package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.DynamicScene;

public class TroopSpawner {

    private MainScene mainScene;

    public TroopSpawner(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public TroopEntity spawnLeftTroop() {
        // Linker Troop van links naar rechts
        return new TroopEntity(new Coordinate2D(0, mainScene.getHeight() - 100), "sprites/troop.png", 50, 2);
    }

    public TroopEntity spawnRightTroop() {
        // Rechter Troop van rechts naar links
        return new TroopEntity(new Coordinate2D(1550, mainScene.getHeight() - 100), "sprites/enemy.png", 30, -2);
    }
}
