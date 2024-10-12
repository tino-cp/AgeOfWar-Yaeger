package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.media.SoundClip;

public class Artillery extends Troop implements Collided, Collider {
    private ProjectileSpawner projectileSpawner;

    private static final double CREDIT_COST = 100;

    public Artillery(Coordinate2D location, String sprite, int team, MainScene mainScene, AgeOfWar ageOfWar) {
        super(location, sprite, team, mainScene, ageOfWar);

        // Stats
        this.hp = 50;
        this.damage = 5;
        this.creditCost = 100;
        this.creditReward = 60;
        this.attackDelay = 5000;

        this.punchSound = new SoundClip("audio/artillery-punch.mp3");

        this.projectileSpawner = new ProjectileSpawner(this);
        mainScene.addEntitySpawner(projectileSpawner);
        attack();

        healthText.updateHealthText();
        healthText.updateHealthTextLocation();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }

    private void attack() {
        projectileSpawner.resume();
    }

    private void stopAttack() {
        if (projectileSpawner.isActive()) {
            projectileSpawner.pause();
        }
    }

    @Override
    protected void manageEnemyMovement(Troop otherTroop) {
        super.manageEnemyMovement(otherTroop);
        if (projectileSpawner.isActive()) {
            stopAttack();
        }
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);

        if (!isAlive()) {
            projectileSpawner.remove();
        }
    }
}