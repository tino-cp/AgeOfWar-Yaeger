package com.github.hanyaeger.ageofwar.entities;

import com.github.hanyaeger.ageofwar.entities.troops.Troop;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.media.SoundClip;

import java.util.List;

public class Rock extends DynamicSpriteEntity implements Collided, Collider, Newtonian {
    private static final double GRAVITY_CONSTANT = 0.015;
    private static final double FRICTION_CONSTANT = 0.0030;

    // Breedte is 40, hoogte wordt automatisch bijgewerkt
    private static final Size ROCK_SIZE = new Size(40, 0);
    private static final int DAMAGE = 500;

    public Rock(Coordinate2D initialLocation, double speed, double angle) {
        super("sprites/rock.png", initialLocation, ROCK_SIZE);
        initializeRock(speed, angle);
    }

    private void initializeRock(double speed, double angle) {
        setGravityConstant(GRAVITY_CONSTANT);
        setFrictionConstant(FRICTION_CONSTANT);
        setMotion(speed, angle);
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop otherTroop) {
                handleCollisionWithTroop(otherTroop);
            } else if (collider instanceof Base base) {
                handleCollisionWithBase(base);
            }
        }
    }

    private void handleCollisionWithTroop(Troop otherTroop) {
        if (otherTroop.getTeam() == 1 && !otherTroop.isDamageTaskScheduled()) {
            otherTroop.takeDamage(DAMAGE);
        } else {
            remove();
        }
    }

    private void handleCollisionWithBase(Base base) {
        if (!base.isFriendlyBase()) {
            applyBaseDamage(base);
        }
    }

    private void applyBaseDamage(Base base) {
        base.takeDamage(DAMAGE / 20);
        base.healthText.updateHealthText();
        playHitSound();
        remove();
    }

    private void playHitSound() {
        SoundClip soundClip = new SoundClip("audio/hit.mp3");
        soundClip.play();
    }
}
