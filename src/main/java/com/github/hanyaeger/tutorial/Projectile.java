package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;

import javafx.scene.paint.Color;

public class Projectile extends DynamicCircleEntity implements Collider, Newtonian {
    private static final double GRAVITY_CONSTANT = 0.015;
    private static final double FRICTION_CONSTANT = 0.0030;
    private static final double RADIUS = 5;
    private static final long SPEED = 4;

    public Projectile(Coordinate2D initialLocation, double angle) {
        super(initialLocation);

        setRadius(RADIUS);
        setMotion(SPEED, angle);
        setGravityConstant(GRAVITY_CONSTANT);
        setFrictionConstant(FRICTION_CONSTANT);
        setFill(Color.BLACK);
    }
}
