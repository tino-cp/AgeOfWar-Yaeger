package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;


public class ProjectileSpawner extends EntitySpawner {
    private Artillery artillery;

    private static final double FRIENDLY_ANGLE = 90d;
    private static final double ENEMY_ANGLE = 270d;
    private static final long INTERVAL = 3000;


    public ProjectileSpawner(Artillery artillery) {
        super(INTERVAL);
        this.artillery = artillery;
    }

    @Override
    protected void spawnEntities() {
        spawnProjectile();
    }

    private void spawnProjectile() {
        Projectile slingshot = null;

        if (artillery.getTeam() == 0) {
            slingshot = new Projectile(new Coordinate2D(artillery.getAnchorLocation().getX() + artillery.getWidth(), artillery.getAnchorLocation().getY()), FRIENDLY_ANGLE, artillery);
        } else if (artillery.getTeam() == 1) {
            slingshot = new Projectile(artillery.getAnchorLocation(), ENEMY_ANGLE, artillery);
        }

        if (slingshot != null) {
            spawn(slingshot);
        }
    }
}