package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.tutorial.TroopEntity;

public class MainScene extends DynamicScene {
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
        TroopEntity troop1 = troopSpawner.spawnLeftTroop();
        TroopEntity troop2 = troopSpawner.spawnRightTroop();

        // Stel de vijanden voor elkaar in
        troop1.setEnemy(troop2);
        troop2.setEnemy(troop1);

        // Voeg de troepen toe aan de scene
        addEntity(troop1);
        addEntity(troop2);

        FloorEntity floor = new FloorEntity(new Coordinate2D(0, 450), this);
        addEntity(floor);
    }
}
