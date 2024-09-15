package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;

import java.util.List;

public class Artillery extends Troop implements Collided, Collider {

    private ProjectileSpawner projectileSpawner;
    private MainScene mainScene;

    public Artillery(Coordinate2D location, String sprite, int hp, double speed, MainScene mainScene) {
        super(location, sprite, hp, speed);
        this.mainScene = mainScene;

        this.projectileSpawner = new ProjectileSpawner(this, 1000);
        System.out.println("Artillery created.");
        attack();
    }

    public ProjectileSpawner getProjectileSpawner() {
        return projectileSpawner;
    }

    @Override
    public void attack() {
        System.out.println("Artillery is attacking.");
        projectileSpawner.resume();  // Resumes the spawner
    }

    @Override
    public void onCollision(List<Collider> colliders) {
        for (Collider collider : colliders) {
            if (collider instanceof Troop enemy) {

                // Stop spawning projectiles
                projectileSpawner.pause();  // Pauses the spawner
                setSpeed(0);  // Stop movement if needed

                break;  // Stop after the first collision with a Troop
            }
        }
    }
}
