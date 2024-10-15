package com.github.hanyaeger.ageofwar.entities.projectiles;

import com.github.hanyaeger.ageofwar.entities.troops.Artillery;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.EntitySpawner;
import com.github.hanyaeger.api.media.SoundClip;


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
        playProjectileSound();
    }

    private void spawnProjectile() {
        Projectile projectile = createProjectile();
        if (projectile != null) {
            spawn(projectile);
        }
    }

    private Projectile createProjectile() {
        Coordinate2D spawnLocation;

        if (artillery.getTeam() == 0) {
            spawnLocation = new Coordinate2D(artillery.getAnchorLocation().getX() + artillery.getWidth(), artillery.getAnchorLocation().getY());
            return new Projectile(spawnLocation, FRIENDLY_ANGLE, artillery);
        } else if (artillery.getTeam() == 1) {
            spawnLocation = artillery.getAnchorLocation();
            return new Projectile(spawnLocation, ENEMY_ANGLE, artillery);
        }

        return null;
    }

    private void playProjectileSound() {
        SoundClip soundClip = new SoundClip("audio/artillery-throw.mp3");
        soundClip.play();
    }
}