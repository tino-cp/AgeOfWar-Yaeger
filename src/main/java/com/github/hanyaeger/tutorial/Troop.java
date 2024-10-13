package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.SceneBorderTouchingWatcher;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.media.SoundClip;
import com.github.hanyaeger.api.scenes.SceneBorder;
import javafx.application.Platform;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Troop extends DynamicSpriteEntity implements Collider, Collided, SceneBorderTouchingWatcher {
    private MainScene mainScene;
    private AgeOfWar ageOfWar;

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
    protected SoundClip punchSound;
    private Timer damageTimer;

    private boolean scheduledForRemoval = false;

    protected Troop(Coordinate2D location, String sprite, int team, MainScene mainScene, AgeOfWar ageOfWar) {
        super(sprite, location);

        this.team = team;
        this.mainScene = mainScene;
        this.ageOfWar = ageOfWar;
        this.speed = (team == 1) ? -DEFAULT_SPEED : DEFAULT_SPEED;

        healthText = new HealthText(this);
        mainScene.setupHealthDisplay(healthText);

        resumeMovement();

        // Gebruik van een ScheduledExecutorService om de Timer en TimerTask te vervangen. Dit komt door de problemen met concurrency.
        damageTimer = new Timer();
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

    public boolean isDamageTaskScheduled() {
        return damageTaskScheduled;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop otherTroop) {
                if (!otherTroop.isAlive() || otherTroop.isScheduledForRemoval()) {
                    continue;
                }
                manageMovement(otherTroop);
            }
        }
    }

    protected void manageMovement(Troop otherTroop) {
        if (isEnemy(otherTroop)) {
            manageEnemyMovement(otherTroop);
        } else if (isFriendly(otherTroop)) {
            manageFriendlyMovement();
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
        } else {
            stopMovement();
        }
    }

    protected void applyDamage(Troop otherTroop) {
        if (!canDealDamage || damageTaskScheduled) {
            return;
        }

        damageTaskScheduled = true;
        canDealDamage = false;

        damageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (isAlive() && otherTroop.isAlive()) {
                        otherTroop.takeDamage(damage);
                        punchSound.play();

                        if (!otherTroop.isAlive()) {
                            onEnemyDeath();
                        }
                    }

                    if (isAlive()) {
                        canDealDamage = true;
                    } else {
                        damageTaskScheduled = false;
                    }
                });
            }
        }, attackDelay, attackDelay);
    }

    protected void onEnemyDeath() {
        if (team == 0) {
            mainScene.setCanTroopsMove(true);
        } else if (team == 1) {
            mainScene.setCanEnemiesMove(true);
        }
        damageTaskScheduled = false;
    }


    protected void takeDamage(int damage) {
        hp -= damage;

        if (!isAlive()) {
            onTroopDeath();
        }
    }

    private void onTroopDeath() {
        SoundClip deathSound = new SoundClip("audio/death.mp3");
        deathSound.play();
        scheduleRemoval();
        canDealDamage = false;
    }

    private void scheduleRemoval() {
        scheduledForRemoval = true;

        if (team == 0) {
            mainScene.troopList.remove(Troop.this);
        } else if (team == 1) {
            mainScene.enemyList.remove(Troop.this);
            mainScene.getCreditText().increaseCredit(creditReward);
        }

        healthText.remove();
        remove();

        checkGameOver();
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

    public void checkGameOver() {
        if (mainScene.enemyList.isEmpty()) {
            damageTimer.cancel();
            ageOfWar.setActiveScene(3);
        }
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
                break;
            default:
                break;
        }
    }
}