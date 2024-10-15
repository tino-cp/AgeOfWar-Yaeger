package com.github.hanyaeger.ageofwar.entities.troops;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.media.SoundClip;

public class Cavalry extends Troop {
    private static final double CREDIT_COST = 150;
    private static final double WIDTH = 89;
    private static final double HEIGHT = 67;

    public Cavalry(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(location, sprite, team, mainScene);
        initializeStats();
        initializeSound();
        healthText.initialize();
    }

    public static double getCreditCostStatic() {
        return CREDIT_COST;
    }

    public static double getCavalryWidth() {
        return WIDTH;
    }

    public static double getCavalryHeight() {
        return HEIGHT;
    }

    private void initializeStats() {
        this.hp = 200;
        this.damage = 25;
        this.creditCost = CREDIT_COST;
        this.creditReward = 100;
        this.attackDelay = 2500;
    }

    private void initializeSound() {
        this.punchSound = new SoundClip("audio/cavalry-stab.mp3");
    }
}
