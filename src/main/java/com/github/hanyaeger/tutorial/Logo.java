package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class Logo extends SpriteEntity {

    public Logo(Coordinate2D location) {
        super("sprites/logo.png", location);
    }
}
