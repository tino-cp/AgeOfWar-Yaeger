package com.github.hanyaeger.ageofwar.entities.abilities;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.ageofwar.entities.Rock;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;

import java.util.Random;

public class AbilitySpawner extends EntitySpawner {
    private MainScene mainScene;
    private Random random = new Random();

    private static final int SPAWN_INTERVAL = 150;
    private static final double OUT_OF_SIGHT_Y = -100;

    public AbilitySpawner(MainScene mainScene) {
        super(SPAWN_INTERVAL);
        this.mainScene = mainScene;
        pause();
    }

    @Override
    protected void spawnEntities(){
        spawnRock();
    }

    private void spawnRock() {
        double direction = getRandomDirection();
        spawn(new Rock(randomLocation(), 2, direction));
    }

    private double getRandomDirection() {
        int randomIndex = random.nextInt(4);
        return switch (randomIndex) {
            case 0 -> 355d;
            case 1 -> 358d;
            case 2 -> 5d;
            case 3 -> 3d;
            default -> 0d;
        };
    }

    private Coordinate2D randomLocation() {
        double x = random.nextDouble(mainScene.getWidth());
        return new Coordinate2D(x, OUT_OF_SIGHT_Y);
    }
}
