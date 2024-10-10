package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;

public class Infantry extends Troop {

    public Infantry(Coordinate2D location, String sprite, int team, MainScene mainscene) {
        super(location, sprite, team, mainscene);

        // Stats
        this.hp = 50;
        this.damage = 10;
        this.creditCost = 50;
        this.creditReward = 30;
        this.attackDelay = 3000;

        healthText.updateHealthText();
        healthText.updateHealthTextLocation();
    }
}
