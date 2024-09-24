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
    protected int hp;
    private double speed;
    protected boolean canDealDamage = true;
    protected Timer damageTimer;
    private int team;
    private boolean isEngagedInCombat = false;

    public Troop(Coordinate2D location, String sprite, int hp, double speed, int team) {
        super(sprite, location);
        this.hp = hp;
        this.speed = speed;
        this.team = team;
        setMotion(speed, 90d);
        damageTimer = new Timer();
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isEngagedInCombat() {
        return isEngagedInCombat;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop troopEntity) {
                Troop otherTroop = troopEntity;
                if (otherTroop != this && canDealDamage && otherTroop.getTeam() != this.getTeam()) {
                    setMotion(0, 0);
                    otherTroop.setMotion(0, 0);
                    applyDamage(otherTroop);
                } else if (otherTroop != this && otherTroop.getTeam() == this.getTeam()) {
                    setMotion(0, 0);
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
                if (isAlive()) {
                    continueWalking();
                }
                isEngagedInCombat = false;
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
        }
    }

    public void continueWalking() {
        setMotion(speed, 90d);
        isEngagedInCombat = false;
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