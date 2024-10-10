package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.scenes.SceneBorder;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Troop extends DynamicSpriteEntity implements Collider, Collided, SceneBorderTouchingWatcher {

    private static final int DEFAULT_SPEED = 2;

    private int speed;
    private int team;

    protected int hp;
    protected int damage;
    protected long attackDelay;
    protected double creditCost;
    protected double creditReward;

    protected boolean canDealDamage = true;
    private boolean damageTaskScheduled = false;

    protected HealthText healthText;
    private MainScene mainScene;

    private boolean scheduledForRemoval = false;

    private final ScheduledExecutorService executorService;

    public Troop(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(sprite, location);

        this.team = team;
        this.mainScene = mainScene;
        this.speed = (team == 1) ? -DEFAULT_SPEED : DEFAULT_SPEED;

        healthText = new HealthText(this);
        mainScene.setupHealthDisplay(healthText);

        resumeMovement();

        // Gebruik van een ScheduledExecutorService om de Timer en TimerTask te vervangen. Dit komt door de problemen met concurrency.
        executorService = Executors.newSingleThreadScheduledExecutor();
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

        if (canDealDamage) {
            healthText.updateHealthText();
            otherTroop.healthText.updateHealthText();
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

        executorService.scheduleAtFixedRate(() -> {
            if (isAlive() && otherTroop.isAlive()) {
                otherTroop.takeDamage(damage);
                System.out.println("Dealt " + damage + " damage to " + otherTroop + ". Remaining HP: " + otherTroop.hp);

                if (!otherTroop.isAlive()) {
                    onTroopDeath();
                }
            }

            if (isAlive()) {
                canDealDamage = true;
            } else {
                damageTaskScheduled = false;
            }
        }, attackDelay, attackDelay, TimeUnit.MILLISECONDS);
    }

    protected void onTroopDeath() {
        if (team == 0) {
            mainScene.setCanTroopsMove(true);
        } else if (team == 1) {
            mainScene.setCanEnemiesMove(true);
        }
        damageTaskScheduled = false;
    }


    protected void takeDamage(int damage) {
        hp -= damage;

        if (hp <= 0) {
            scheduleRemoval();
            canDealDamage = false;
        }
    }

    // Wegens concurrency problemen is het beter om de verwijdering van de Troop te schedulen
    private void scheduleRemoval() {
        scheduledForRemoval = true;

        executorService.schedule(() -> {
            if (team == 0) {
                mainScene.troopList.remove(Troop.this);
            } else if (team == 1) {
                mainScene.enemyList.remove(Troop.this);
                mainScene.getCreditText().increaseCredit(creditReward);
            }

            remove();
            healthText.remove();
        }, 1, TimeUnit.MILLISECONDS);
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