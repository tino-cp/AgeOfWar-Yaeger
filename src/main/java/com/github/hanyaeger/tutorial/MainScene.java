package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.tutorial.TroopEntity;

public class MainScene extends DynamicScene implements EntitySpawnerContainer {

    private Artillery artillery;
    private Troop troop;

    public MainScene(AgeOfWar ageOfWar) {
    }

    @Override
    public void setupScene() {
        //setBackgroundAudio("audio/AgeOfWar_ThemeSong.mp3");
        setBackgroundImage("backgrounds/background.png");
    }

    @Override
    public void setupEntities() {
        // Maak een TroopSpawner object aan
        TroopSpawner troopSpawner = new TroopSpawner(this);

        // Spawn de linker en rechter troepen via de spawner
        troop = new TroopEntity(new Coordinate2D(0, getHeight() - 100), "sprites/troop.png", 50, 2);
        addEntity(troop);

        Troop troop2 = new TroopEntity(new Coordinate2D(1450, getHeight() - 100), "sprites/troop2.png", 30, -2);
        addEntity(troop2);

        troop.setEnemy(troop2);
        troop2.setEnemy(troop);

        FloorEntity floor = new FloorEntity(new Coordinate2D(0, 450), this);
        addEntity(floor);
    }


    @Override
    public void setupEntitySpawners() {
        if (artillery != null) {
            addEntitySpawner(artillery.getProjectileSpawner());
            System.out.print("Added Artillery projectile spawner");
        }
    }
}
