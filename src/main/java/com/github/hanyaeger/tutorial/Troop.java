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
    private MainScene mainScene;
    private HealthDisplay healthDisplay;

    public Troop(Coordinate2D location, String sprite, int hp, double speed, int team, MainScene mainScene) {
        super(sprite, location);
        this.hp = hp;
        this.speed = speed;
        this.team = team;
        this.mainScene = mainScene;
        setMotion(speed, 90d);
        damageTimer = new Timer();

        healthDisplay = new HealthDisplay(this);
        mainScene.addHealthDisplay(healthDisplay);
    }

    public int getTeam() {
        return team;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getHp() {
        return hp;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop otherTroop) {
                if (isEnemy(otherTroop)) {
                    manageEnemyMovement(otherTroop);
                } else if (isFriendly(otherTroop)) {
                    manageFriendlyMovement();
                }
            }
        }
    }

    protected void manageEnemyMovement(Troop otherTroop) {
        stopMovement();

        if (team == 0) {
            mainScene.setCanTroopsMove(false);
        } else if (team == 1) {
            mainScene.setCanEnemiesMove(false);
        }

        if (canDealDamage && otherTroop.canDealDamage) {
            applyDamage(otherTroop);
            otherTroop.applyDamage(this);
        }
    }

    protected void manageFriendlyMovement() {
        if (this.getTeam() == 0 && !mainScene.canTroopsMove()) {
            stopMovement();
        } else if (this.getTeam() == 1 && !mainScene.canEnemiesMove()) {
            stopMovement();
        } else {
            resumeMovement();
        }
    }

    public void updateMovement() {
        if (mainScene.canTroopsMove() && team == 0 || mainScene.canEnemiesMove() && team == 1) {
            resumeMovement();
            System.out.println("Moving " + (team == 0 ? "troop" : "enemy") + " forward");
        } else {
            stopMovement();
            System.out.println((team == 0 ? "Troop" : "Enemy") + " stopped");
        }
    }


    protected void applyDamage(Troop otherTroop) {
        canDealDamage = false;

        TimerTask damageTask = new TimerTask() {
            @Override
            public void run() {
                if (isAlive() && otherTroop.isAlive()) {
                    otherTroop.takeDamage(10);

                    System.out.println("Dealt 10 damage to " + otherTroop + ". Remaining HP: " + otherTroop.hp);

                    if (!otherTroop.isAlive()) {
                        if (team == 0) {
                            mainScene.setCanTroopsMove(true);
                        } else if (team == 1) {
                            mainScene.setCanEnemiesMove(true);
                        }
                    }
                } else {
                    cancel();
                }
                if (isAlive()) {
                    canDealDamage = true;
                }
            }
        };

        damageTimer.schedule(damageTask, 3000, 3000);
    }

    protected void takeDamage(int damage) {
        hp -= damage;
        healthDisplay.updateHealth();
        if (hp <= 0) {
            remove();
            healthDisplay.remove();

            if (team == 0) {
                mainScene.troopList.remove(this);
            } else if (team == 1) {
                mainScene.enemyList.remove(this);
            }

            damageTimer.cancel();
        }
    }

    protected void stopMovement() {
        setMotion(0, 0);
    }

    protected void resumeMovement() {
        setMotion(speed, 90d);
    }

    protected boolean isEnemy(Troop otherTroop) {
        return otherTroop.getTeam() != this.getTeam();
    }

    protected boolean isFriendly(Troop otherTroop) {
        return otherTroop.getTeam() == this.getTeam();
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
}