package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;

import java.util.List;
import java.util.TimerTask;

public class Artillery extends Troop implements Collided, Collider {

    private ProjectileSpawner projectileSpawner;

    public Artillery(Coordinate2D location, String sprite, int hp, double speed, MainScene mainScene, int team) {
        super(location, sprite, hp, speed, team);


        this.projectileSpawner = new ProjectileSpawner(this, 3000);
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
                if (otherTroop != this && canDealDamage && otherTroop.getTeam() != this.getTeam()) {
                    setMotion(0, 0);
                    otherTroop.setMotion(0, 0);
                    applyDamage(otherTroop);

                    if (projectileSpawner.isActive()) {
                        projectileSpawner.resume();
                    } else {
                        projectileSpawner.pause();
                    }
                } else if (otherTroop != this && otherTroop.getTeam() == this.getTeam()) {
                    setMotion(0, 0);
                }
            }
        }
    }

    protected void applyDamage(Troop otherTroop) {
        setMotion(0, 0);
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

    @Override
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            remove();
            projectileSpawner.remove();
        }
    }
}