package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.media.SoundClip;

public class Infantry extends Troop {
    private static final double CREDIT_COST = 50;

    public Infantry(Coordinate2D location, String sprite, int team, MainScene mainScene, AgeOfWar ageOfWar) {
        super(location, sprite, team, mainScene, ageOfWar);

        // Stats
        this.hp = 50;
        this.damage = 10;
        this.creditCost = 50;
        this.creditReward = 30;
        this.attackDelay = 3000;

        this.punchSound = new SoundClip("audio/infantry-bonk.mp3");

        healthText.updateHealthText();
        healthText.updateHealthTextLocation();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }
}
