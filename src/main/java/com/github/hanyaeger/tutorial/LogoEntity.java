package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class LogoEntity extends SpriteEntity {

    public LogoEntity(Coordinate2D location) {
        super("sprites/logo.png", location);
    }
}
