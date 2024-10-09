package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;

public class TroopEntity extends Troop {

    public TroopEntity(Coordinate2D location, String sprite, int hp, double speed, int team, MainScene mainscene) {
        super(location, sprite, hp, speed, team, mainscene);
    }
}
