package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;

import javafx.scene.paint.Color;

import java.util.List;

public class Projectile extends DynamicCircleEntity implements Collided, Collider, Newtonian {
    private static final double GRAVITY_CONSTANT = 0.015;
    private static final double FRICTION_CONSTANT = 0.0030;
    private static final double RADIUS = 5;
    private static final double SPEED = 4;
    private static final int DAMAGE = 1;

    private Artillery artillery;

    public Projectile(Coordinate2D initialLocation, double angle, Artillery artillery) {
        super(initialLocation);

        this.artillery = artillery;

        setRadius(RADIUS);
        setMotion(SPEED, angle);
        setGravityConstant(GRAVITY_CONSTANT);
        setFrictionConstant(FRICTION_CONSTANT);
        setFill(Color.BLACK);
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop enemyTroop) {
                if (artillery.isEnemy(enemyTroop)) {
                    applyConcussiveDamage(enemyTroop);
                    remove();
                }
            }
        }
    }

    private void applyConcussiveDamage(Troop enemyTroop) {
        // Slingshot kan een enemy niet volledig doden. Dit is een soort van concussive damage.
        if (enemyTroop.getHp() > 5) {
            enemyTroop.takeDamage(DAMAGE);
            artillery.healthText.updateHealthText();
        }
    }
}
