package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;

public class TroopEntity extends Troop {

    public TroopEntity(Coordinate2D location, String sprite, int hp, double speed) {
        super(location, sprite, hp, speed);
    }

    @Override
    public void attack() {

    }
}
