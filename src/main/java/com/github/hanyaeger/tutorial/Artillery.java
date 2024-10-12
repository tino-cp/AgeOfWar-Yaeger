package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;

import java.util.List;

public class Artillery extends Troop implements Collided, Collider {
    private ProjectileSpawner projectileSpawner;

    public Artillery(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(location, sprite, team, mainScene);

        // Stats
        this.hp = 50;
        this.damage = 5;
        this.creditCost = 100;
        this.creditReward = 60;
        this.attackDelay = 5000;

        this.projectileSpawner = new ProjectileSpawner(this);
        mainScene.addEntitySpawner(projectileSpawner);
        attack();

        healthText.updateHealthText();
        healthText.updateHealthTextLocation();
    }

    private void attack() {
        projectileSpawner.resume();
    }

    private void stopAttack() {
        projectileSpawner.pause();
    }

    @Override
    public void onCollision(List<Collider> colliders) {
        for (Collider collider : colliders) {
            if (collider instanceof Troop otherTroop) {
                if (isEnemy(otherTroop)) {
                    if (projectileSpawner.isActive()) {
                        stopAttack();
                    }

                    manageEnemyMovement(otherTroop);
                } else if (isFriendly(otherTroop)) {
                    manageFriendlyMovement();
                } else {
                    attack();
                }
            }
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