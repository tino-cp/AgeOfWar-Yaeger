package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;

public class Cavalry extends Troop {
    private static final double Y_OFFSET = 24.0;

    public Cavalry(Coordinate2D location, String sprite, double speed, int team, MainScene mainScene) {
        super(location, sprite, speed, team, mainScene);
        setAnchorLocationY(location.getY() - Y_OFFSET);

        this.hp = 200;
        this.damage = 25;
        this.creditCost = 150;
        this.creditReward = 100;
        this.attackDelay = 2000;

        healthText.updateHealthDisplay();
    }
}
