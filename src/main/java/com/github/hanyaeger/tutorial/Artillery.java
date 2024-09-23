package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;

import java.util.List;
import java.util.TimerTask;

public class Artillery extends Troop implements Collided, Collider {

    private ProjectileSpawner projectileSpawner;

    public Artillery(Coordinate2D location, String sprite, int hp, double speed, MainScene mainScene) {
        super(location, sprite, hp, speed);

        this.projectileSpawner = new ProjectileSpawner(this, 1000);
        attack(mainScene);
    }

    @Override
    public void attack(MainScene mainScene) {
        projectileSpawner.resume();
        mainScene.addEntitySpawner(projectileSpawner);
    }

    @Override
    public void onCollision(List<Collider> colliders) {
        for (Collider collider : colliders) {
            if (collider instanceof Troop troopEntity) {
                Troop otherTroop = troopEntity;
                setMotion(0, 0);
                if (otherTroop != this && canDealDamage && otherTroop.getTeam() != this.getTeam()) {
                    applyDamage(otherTroop);

                    if (!projectileSpawner.isActive()) {
                        projectileSpawner.resume();
                    } else {
                        projectileSpawner.pause();
                    }
                }
            }
        }
    }

    protected void applyDamage(Troop otherTroop) {
        canDealDamage = false;

        if (isAlive() && otherTroop.isAlive()) {
            otherTroop.takeDamage(10);

            if (!otherTroop.isAlive()) {
                continueWalking();
            }
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                canDealDamage = true;
            }
        };

        damageTimer.schedule(task, 3000);
    }
}