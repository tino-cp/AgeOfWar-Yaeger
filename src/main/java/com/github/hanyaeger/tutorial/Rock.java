package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;

public class Rock extends DynamicSpriteEntity implements Collider, Newtonian {
    private static final double GRAVITY_CONSTANT = 0.015;
    private static final double FRICTION_CONSTANT = 0.0030;

    // Size werkt wat raar, als 1 van de 2 niet is ingevuld, dan maakt hij de andere automatisch in verhouding
    private static final Size ROCK_SIZE = new Size(40, 0);

    protected Rock(Coordinate2D initialLocation, double speed, double angle) {
        super("sprites/rock.png", initialLocation, ROCK_SIZE);

        setGravityConstant(GRAVITY_CONSTANT);
        setFrictionConstant(FRICTION_CONSTANT);
        setMotion(speed, angle);
    }
}
