package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.scenes.DynamicScene;

import java.util.*;

public class MainScene extends DynamicScene implements EntitySpawnerContainer {

    private SpawnTroopButton spawnTroop1Button, spawnTroop2Button;
    private Queue<Troop> troopQueue = new LinkedList<>();
    private int maxTroopCount = 10;
    private int spawnXLimit = 100;

    public MainScene(AgeOfWar ageOfWar) {
    }

    @Override
    public void setupScene() {
        //setBackgroundAudio("audio/AgeOfWar_ThemeSong.mp3");
        setBackgroundImage("backgrounds/background.png");
    }

    @Override
    public void setupEntities() {
        spawnTroop1Button = new SpawnTroopButton(new Coordinate2D(50, 50), "sprites/troop1-icon.png", 0, this);
        spawnTroop2Button = new SpawnTroopButton(new Coordinate2D(150, 50), "sprites/troop2-icon.png", 1, this);

        addEntity(spawnTroop1Button);
        addEntity(spawnTroop2Button);

        FloorEntity floor = new FloorEntity(new Coordinate2D(0, 450), this);
        addEntity(floor);
    }

    @Override
    public void setupEntitySpawners() {

    }

    public void spawnTroop(int troopType) {
        if (troopQueue.size() >= maxTroopCount || getFrontLineTroops().stream().anyMatch(troop -> troop.getAnchorLocation().getX() < spawnXLimit)) {
            return;
        }

        Troop newTroop = null;
        switch (troopType) {
            case 0:
                newTroop = new TroopEntity(new Coordinate2D(0, getHeight() - 100), "sprites/troop.png", 50, 2);
                break;
            case 1:
                newTroop = new Artillery(new Coordinate2D(0, getHeight() - 100), "sprites/troop2.png", 20, 2, this);
                break;
        }

        if (newTroop != null) {
            troopQueue.add(newTroop);
            addEntity(newTroop);
        }
    }

    private List<Troop> getFrontLineTroops() {
        List<Troop> frontLineTroops = new ArrayList<>();
        double minX = Integer.MAX_VALUE;
        for (Troop troop : troopQueue) {
            if (troop.getAnchorLocation().getX() < minX) {
                minX = troop.getAnchorLocation().getX();
            }
        }
        for (Troop troop : troopQueue) {
            if (troop.getAnchorLocation().getX() == minX) {
                frontLineTroops.add(troop);
            }
        }
        return frontLineTroops;
    }
}
