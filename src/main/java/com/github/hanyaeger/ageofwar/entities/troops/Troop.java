package com.github.hanyaeger.ageofwar.entities.troops;

import com.github.hanyaeger.ageofwar.entities.Base;
import com.github.hanyaeger.ageofwar.entities.Damageable;
import com.github.hanyaeger.ageofwar.entities.texts.HealthText;
import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
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

public abstract class Troop extends DynamicSpriteEntity implements Collider, Collided, SceneBorderTouchingWatcher, Damageable {
    private MainScene mainScene;
    private Timer damageTimer;

    private static final int DEFAULT_SPEED = 2;
    private static final int WIDTH = 43;
    private static final int HEIGHT = 43;

    private int speed;
    private int team;
    private boolean scheduledForRemoval = false;

    protected int hp;
    protected int damage;
    protected long attackDelay;
    protected double creditCost;
    protected double creditReward;

    protected boolean canDealDamage = true;
    private boolean damageTaskScheduled = false;

    public HealthText healthText;
    protected SoundClip punchSound;

    protected Troop(Coordinate2D location, String sprite, int team, MainScene mainScene) {
        super(sprite, location);
        this.team = team;
        this.mainScene = mainScene;
        this.speed = (team == 1) ? -DEFAULT_SPEED : DEFAULT_SPEED;

        damageTimer = new Timer();
        healthText = new HealthText(this);
        mainScene.setupHealthDisplay(healthText);

        resumeMovement();
    }

    public static double getTroopWidth() {
        return WIDTH;
    }

    public static double getTroopHeight() {
        return HEIGHT;
    }

    public int getTeam() {
        return team;
    }

    @Override
    public boolean isAlive() {
        return hp > 0;
    }

    public int getHp() {
        return hp;
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
                manageMovement(otherTroop);
            } else if (collider instanceof Base base) {
                handleBaseCollision(base);
            }
        }
    }

    private void handleBaseCollision(Base base) {
        if (isEnemyBase(base)) {
            stopMovement();
            updateTroopMovementFlag();

            if (canDealDamage) {
                base.healthText.updateHealthText();
                applyDamage(base);
            }
        }
    }

    private boolean isEnemyBase(Base base) {
        return (team == 0 && !base.isFriendlyBase()) || (team == 1 && base.isFriendlyBase());
    }

    protected void manageMovement(Troop otherTroop) {
        if (otherTroop.isAlive() && !otherTroop.isScheduledForRemoval()) {
            if (isEnemy(otherTroop)) {
                manageEnemyMovement(otherTroop);
            } else if (isFriendly(otherTroop)) {
                manageFriendlyMovement();
            }
        }
    }

    protected void manageEnemyMovement(Troop otherTroop) {
        stopMovement();
        updateTroopMovementFlag();

        if (canDealDamage) {
            healthText.updateHealthText();
            otherTroop.healthText.updateHealthText();
            applyDamage(otherTroop);
            otherTroop.applyDamage(this);
        }
    }

    protected void manageFriendlyMovement() {
        if (shouldStopMovement()) {
            stopMovement();
        } else {
            resumeMovement();
        }
    }

    private boolean shouldStopMovement() {
        return (team == 0 && !mainScene.canTroopsMove()) || (team == 1 && !mainScene.canEnemiesMove());
    }

    private void updateTroopMovementFlag() {
        if (team == 0) {
            mainScene.setCanTroopsMove(false);
        } else {
            mainScene.setCanEnemiesMove(false);
        }
    }

    public void updateMovement() {
        if (mainScene.canTroopsMove() && team == 0 || mainScene.canEnemiesMove() && team == 1) {
            resumeMovement();
        } else {
            stopMovement();
        }
    }

    protected void applyDamage(Damageable target) {
        if (!canDealDamage || damageTaskScheduled) {
            return;
        }

        damageTaskScheduled = true;
        canDealDamage = false;

        damageTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (isAlive() && target.isAlive()) {
                        target.takeDamage(damage);
                        punchSound.play();

                        if (!target.isAlive()) {
                            onTargetDeath();
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


    protected void onTargetDeath() {
        mainScene.setCanTroopsMove(true);
        mainScene.setCanEnemiesMove(true);
        damageTaskScheduled = false;
    }

    @Override
    public void takeDamage(int damage) {
        hp -= damage;

        if (!isAlive()) {
            onTroopDeath();
        }
    }

    private void onTroopDeath() {
        new SoundClip("audio/death.mp3").play();
        checkRemoval();
        canDealDamage = false;
    }

    private void checkRemoval() {
        scheduledForRemoval = true;
        removeFromScene();

        healthText.remove();
        remove();
    }

    private void removeFromScene() {
        if (team == 0) {
            mainScene.troopList.remove(this);
        } else {
            mainScene.enemyList.remove(this);
            mainScene.getCreditText().increaseCredit(creditReward);
        }
    }

    protected void stopMovement() {
        setMotion(0, 0);
        healthText.setMotion(0, 0);
    }

    protected void resumeMovement() {
        setMotion(speed, 90d);
        healthText.setMotion(speed, 90d);
    }

    public boolean isEnemy(Troop otherTroop) {
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
                break;
            default:
                break;
        }
    }
}