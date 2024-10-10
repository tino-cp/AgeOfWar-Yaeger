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

    protected int hp = 50;
    protected int damage = 10;
    protected long attackDelay = 3000;
    protected double creditCost = 50;
    protected double creditReward = 30;

    private double speed;
    private int team;

    protected boolean canDealDamage = true;
    protected Timer damageTimer;
    protected HealthText healthText;
    private MainScene mainScene;

    private boolean scheduledForRemoval = false;

    private boolean damageTaskScheduled = false;

    public Troop(Coordinate2D location, String sprite, double speed, int team, MainScene mainScene) {
        super(sprite, location);

        this.speed = speed;
        this.team = team;
        this.mainScene = mainScene;

        damageTimer = new Timer();
        healthText = new HealthText(this);
        mainScene.setupHealthDisplay(healthText);
        resumeMovement();
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

    public double getCreditCost() {
        return creditCost;
    }

    public boolean isScheduledForRemoval() {
        return scheduledForRemoval;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop otherTroop) {
                if (!otherTroop.isAlive() || otherTroop.isScheduledForRemoval()) {
                    continue;
                }
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
            healthText.updateHealthDisplay();
            otherTroop.healthText.updateHealthDisplay();
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
        if (!canDealDamage || damageTaskScheduled) {
            return;
        }

        damageTaskScheduled = true;
        canDealDamage = false;

        TimerTask damageTask = new TimerTask() {
            @Override
            public void run() {
                if (isAlive() && otherTroop.isAlive()) {
                    otherTroop.takeDamage(damage);

                    System.out.println("Dealt 10 damage to " + otherTroop + ". Remaining HP: " + otherTroop.hp);
                    System.out.println("Troop of team: " + getTeam() + "attack delay is: " + attackDelay);

                    if (!otherTroop.isAlive()) {
                        if (team == 0) {
                            mainScene.setCanTroopsMove(true);
                        } else if (team == 1) {
                            mainScene.setCanEnemiesMove(true);
                        }
                        cancel();
                    }
                }

                if (isAlive()) {
                    canDealDamage = true;
                } else {
                    cancel();
                }
            }

            @Override
            public boolean cancel() {
                damageTaskScheduled = false;
                return super.cancel();
            }
        };

        damageTimer.schedule(damageTask, attackDelay, attackDelay);
    }

    protected void takeDamage(int damage) {
        hp -= damage;

        if (hp <= 0) {
            scheduleRemoval();
        }
    }

    // Wegens concurrency problemen is het beter om de verwijdering van de Troop te schedulen
    private void scheduleRemoval() {
        scheduledForRemoval = true;

        TimerTask removalTask = new TimerTask() {
            @Override
            public void run() {
                if (team == 0) {
                    mainScene.troopList.remove(Troop.this);
                } else if (team == 1) {
                    mainScene.enemyList.remove(Troop.this);
                    mainScene.getCreditText().increaseCredit(creditReward);
                }

                remove();
                healthText.remove();
            }
        };
        new Timer().schedule(removalTask, 1);
    }

    protected void stopMovement() {
        setMotion(0, 0);
        healthText.setMotion(0, 0);
    }

    protected void resumeMovement() {
        setMotion(speed, 90d);
        healthText.setMotion(speed, 90d);
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