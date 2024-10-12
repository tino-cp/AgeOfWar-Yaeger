package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.media.SoundClip;

public class Cavalry extends Troop {
    private static final double Y_OFFSET = 24.0;
    private static final double CREDIT_COST = 150;

    public Cavalry(Coordinate2D location, String sprite, int team, MainScene mainScene, AgeOfWar ageOfWar) {
        super(location, sprite, team, mainScene, ageOfWar);
        setAnchorLocationY(location.getY() - Y_OFFSET);

        // Stats
        this.hp = 200;
        this.damage = 25;
        this.creditCost = 150;
        this.creditReward = 100;
        this.attackDelay = 2000;

        this.punchSound = new SoundClip("audio/cavalry-stab.mp3");

        healthText.updateHealthText();
        healthText.updateHealthTextLocation();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }
}
