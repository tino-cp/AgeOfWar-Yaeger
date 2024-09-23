package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.scenes.SceneBorder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Troop extends DynamicSpriteEntity implements Collider, Collided, SceneBorderTouchingWatcher {
    private int hp;
    private double speed;
    protected boolean canDealDamage = true;
    private Troop enemy;
    protected Timer damageTimer;
    private int team;

    public Troop(Coordinate2D location, String sprite, int hp, double speed) {
        super(sprite, location);
        this.hp = hp;
        this.speed = speed;
        setMotion(speed, 90d);
        damageTimer = new Timer();
    }

    public void setEnemy(Troop enemy) {
        this.enemy = enemy;
    }

    public int getTeam() {
        return team;
    }


    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop troopEntity) {
                Troop otherTroop = troopEntity;
                setMotion(0, 0);
                if (otherTroop != this && canDealDamage && otherTroop.getTeam() != this.getTeam()) {
                    applyDamage(otherTroop);
                }
            }
        }
    }

    private void applyDamage(Troop otherTroop) {

        setMotion(0, 0);
        canDealDamage = false;

        if (isAlive() && otherTroop.isAlive()) {
            otherTroop.takeDamage(10);

            if (!otherTroop.isAlive()) {
                continueWalking();
            }
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                canDealDamage = true;
            }
        };

        damageTimer.schedule(task, 3000);
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            remove();

            if (enemy != null && enemy.isAlive()) {
                enemy.continueWalking();
            }
        }
    }

    public void continueWalking() {
        setMotion(speed, 90d);
    }

    public boolean isAlive() {
        return hp > 0;
    }


    @Override
    public void notifyBoundaryTouching(SceneBorder border) {
        switch (border) {
            case TOP:
                setAnchorLocationY(1);
                break;
            case LEFT:
                setAnchorLocationX(1);
                break;
            case RIGHT:
                setAnchorLocationX(getSceneWidth() - getWidth());
            default:
                break;
        }
    }

    public abstract void attack(MainScene mainScene);
}