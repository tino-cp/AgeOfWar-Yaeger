package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;


public class ProjectileSpawner extends EntitySpawner {
    private Artillery artillery;

    public ProjectileSpawner(Artillery artillery, long interval) {
        super(interval);

        this.artillery = artillery;
    }

    @Override
    protected void spawnEntities() {
        Projectile slingshot = new Projectile(artillery.getLocation(), artillery, 5, 3);

        spawn(slingshot);
    }
}