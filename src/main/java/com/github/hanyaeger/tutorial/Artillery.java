package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;

import java.util.List;
import java.util.TimerTask;

public class Artillery extends Troop implements Collided, Collider {
    private ProjectileSpawner projectileSpawner;

    public Artillery(Coordinate2D location, String sprite, int hp, double speed, int team, MainScene mainScene) {
        super(location, sprite, hp, speed, team, mainScene);

        this.projectileSpawner = new ProjectileSpawner(this);
        attack(mainScene);
    }

    private void attack(MainScene mainScene) {
        projectileSpawner.resume();
        mainScene.addEntitySpawner(projectileSpawner);
    }

    @Override
    public void onCollision(List<Collider> colliders) {
        for (Collider collider : colliders) {
            if (collider instanceof Troop otherTroop) {
                if (isEnemy(otherTroop)) {
                    if (projectileSpawner.isActive()) {
                        projectileSpawner.resume();
                    } else {
                        projectileSpawner.pause();
                    }
                    manageEnemyMovement(otherTroop);

                } else if (isFriendly(otherTroop)) {
                    manageFriendlyMovement();
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