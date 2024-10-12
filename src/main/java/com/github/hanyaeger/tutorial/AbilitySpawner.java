package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;

import java.util.Random;

public class AbilitySpawner extends EntitySpawner {

    private MainScene mainScene;
    private Random random = new Random();

    public AbilitySpawner(MainScene mainScene) {
        super(150);

        this.mainScene = mainScene;
        pause();
    }

    @Override
    protected void spawnEntities(){
        spawnRock();
    }

    private void spawnRock() {
        int randomIndex = random.nextInt(4);
        double direction = switch (randomIndex) {
            case 0 -> 355d;
            case 1 -> 358d;
            case 2 -> 5d;
            case 3 -> 3d;
            default -> 0d;
        };
        spawn(new Rock(randomLocation(), 2, direction));
    }

    private Coordinate2D randomLocation() {
        // Het y-coördinaat is buiten het scherm, zodat je de rotsen niet ziet spawnen
        double y = -100;
        double x = random.nextDouble(mainScene.getWidth());

        return new Coordinate2D(x, y);
    }
}
