package com.github.hanyaeger.ageofwar.entities.troops;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.ageofwar.entities.projectiles.ProjectileSpawner;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.media.SoundClip;

public class Artillery extends Troop implements Collided, Collider {
    private static final double CREDIT_COST = 100;

    private ProjectileSpawner projectileSpawner;

    public Artillery(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(location, sprite, team, mainScene);
        initializeStats();
        initializeSound();
        initializeProjectileSpawner(mainScene);
        healthText.initialize();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }

    private void initializeStats() {
        this.hp = 50;
        this.damage = 5;
        this.creditCost = CREDIT_COST;
        this.creditReward = 60;
        this.attackDelay = 4000;
    }

    private void initializeSound() {
        this.punchSound = new SoundClip("audio/artillery-punch.mp3");
    }

    private void initializeProjectileSpawner(MainScene mainScene) {
        this.projectileSpawner = new ProjectileSpawner(this);
        mainScene.addEntitySpawner(projectileSpawner);
        startShooting();
    }

    private void startShooting() {
        projectileSpawner.resume();
    }

    private void stopShooting() {
        if (projectileSpawner.isActive()) {
            projectileSpawner.pause();
        }
    }

    @Override
    protected void manageEnemyMovement(Troop otherTroop) {
        super.manageEnemyMovement(otherTroop);
        stopShooting();
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (!isAlive()) {
            projectileSpawner.remove();
        }
    }
}
