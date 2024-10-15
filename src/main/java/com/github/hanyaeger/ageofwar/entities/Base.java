package com.github.hanyaeger.ageofwar.entities;

import com.github.hanyaeger.ageofwar.AgeOfWar;
import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.ageofwar.entities.texts.HealthText;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class Base extends SpriteEntity implements Collider, Damageable {
    private AgeOfWar ageOfWar;
    public HealthText healthText;

    private static final double WIDTH = 232;
    private static final double HEIGHT = 169;

    private boolean isFriendlyBase;
    private int hp = 500;

    public Base(String resource, Coordinate2D initialLocation, boolean isFriendlyBase, MainScene mainScene, AgeOfWar ageOfWar) {
        super(resource, initialLocation);
        this.isFriendlyBase = isFriendlyBase;
        this.ageOfWar = ageOfWar;

        initializeHealthText(mainScene);
    }

    public static double getBaseWidth() {
        return WIDTH;
    }

    public static double getBaseHeight() {
        return HEIGHT;
    }

    public int getHp() {
        return hp;
    }

    public boolean isFriendlyBase() {
        return isFriendlyBase;
    }

    private void initializeHealthText(MainScene mainScene) {
        healthText = new HealthText(this);
        mainScene.setupHealthDisplay(healthText);
        healthText.updateHealthTextLocation();
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (!isAlive()) {
            checkGameOver();
        }
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    private void checkGameOver() {
        int sceneIndex = isFriendlyBase ? 2 : 3;
        ageOfWar.setActiveScene(sceneIndex);
    }
}
