package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicCircleEntity;
import javafx.scene.paint.Color;


public class Projectile extends DynamicCircleEntity implements Collider, Newtonian {
    private Artillery artillery;

    public Projectile(Coordinate2D initialLocation, Artillery artillery, double radius, double speed) {
        super(initialLocation);

        this.artillery = artillery;

        setRadius(radius);
        setMotion(speed, 100d);
        setGravityConstant(0.010);
        setFrictionConstant(0.0025);
        setFill(Color.BLACK);
    }
}
