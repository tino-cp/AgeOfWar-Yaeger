package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;

public class TroopEntity extends Troop {

    public TroopEntity(Coordinate2D location, String sprite, int hp, double speed, int team) {
        super(location, sprite, hp, speed, team);
    }

    @Override
    public void attack(MainScene mainScene) {

    }
}
