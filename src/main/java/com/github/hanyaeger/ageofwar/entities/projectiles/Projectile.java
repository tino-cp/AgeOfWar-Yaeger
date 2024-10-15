package com.github.hanyaeger.ageofwar.entities.projectiles;

import com.github.hanyaeger.ageofwar.entities.Base;
import com.github.hanyaeger.ageofwar.entities.troops.Artillery;
import com.github.hanyaeger.ageofwar.entities.troops.Troop;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;

import com.github.hanyaeger.api.media.SoundClip;
import javafx.scene.paint.Color;

import java.util.List;

public class Projectile extends DynamicCircleEntity implements Collided, Collider, Newtonian {
    private static final double GRAVITY_CONSTANT = 0.015;
    private static final double FRICTION_CONSTANT = 0.0030;
    private static final double RADIUS = 5;
    private static final double SPEED = 4;
    private static final int DAMAGE = 1;
    private static final int CONCUSSION_HEALTH_THRESHOLD = 5;

    private Artillery artillery;

    public Projectile(Coordinate2D initialLocation, double angle, Artillery artillery) {
        super(initialLocation);
        this.artillery = artillery;

        initializeProjectile();
        setMotion(SPEED, angle);
    }

    private void initializeProjectile() {
        setRadius(RADIUS);
        setGravityConstant(GRAVITY_CONSTANT);
        setFrictionConstant(FRICTION_CONSTANT);
        setFill(Color.BLACK);
    }

    @Override
    public void onCollision(List<Collider> colliders) {
        for (Collider collider : colliders) {
            if (collider instanceof Troop enemyTroop) {
                handleTroopCollision(enemyTroop);
            } else if (collider instanceof Base base) {
                handleBaseCollision(base);
            }
        }
    }

    private void handleBaseCollision(Base base) {
        if (artillery.getTeam() == 1 && base.isFriendlyBase() || artillery.getTeam() == 0 && !base.isFriendlyBase()) {
            applyBaseDamage(base);
        }
    }

    private void applyBaseDamage(Base base) {
        base.takeDamage(DAMAGE);
        base.healthText.updateHealthText();
        playHitSound();
        remove();
    }

    private void handleTroopCollision(Troop enemyTroop) {
        if (artillery.isEnemy(enemyTroop)) {
            applyConcussiveDamage(enemyTroop);
            remove();
        }
    }

    private void applyConcussiveDamage(Troop enemyTroop) {
        // Slingshot kan een enemy niet volledig doden. Dit is een soort van concussive damage.
        if (enemyTroop.getHp() > CONCUSSION_HEALTH_THRESHOLD) {
            enemyTroop.takeDamage(DAMAGE);
            enemyTroop.healthText.updateHealthText();
            playHitSound();
        }
    }

    private void playHitSound() {
        SoundClip soundClip = new SoundClip("audio/hit.mp3");
        soundClip.play();
    }
}
