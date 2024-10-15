package com.github.hanyaeger.ageofwar.entities.troops;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.media.SoundClip;

public class Infantry extends Troop {
    private static final double CREDIT_COST = 50;

    public Infantry(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(location, sprite, team, mainScene);
        initializeStats();
        initializeSound();
        healthText.initialize();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }

    private void initializeStats() {
        this.hp = 50;
        this.damage = 10;
        this.creditCost = CREDIT_COST;
        this.creditReward = 30;
        this.attackDelay = 3000;
    }

    private void initializeSound() {
        this.punchSound = new SoundClip("audio/infantry-bonk.mp3");
    }
}
