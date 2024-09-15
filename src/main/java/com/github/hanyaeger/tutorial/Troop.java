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
    protected double speed;
    protected boolean canDealDamage = true;
    protected Troop enemy;
    private Timer damageCooldownTimer;
    private TimerTask damageTask;

    public Troop(Coordinate2D location, String sprite, int hp, double speed) {
        super(sprite, location);
        this.hp = hp;
        this.speed = speed;
        setMotion(speed, 90d);  // Beweeg in een rechte lijn
        initializeDamageCooldownTimer();
    }

    private void initializeDamageCooldownTimer() {
        damageCooldownTimer = new Timer();
    }

    public void setEnemy(Troop enemy) {
        this.enemy = enemy;
    }

    public void setCanDealDamage(boolean canDealDamage) {
        this.canDealDamage = canDealDamage;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Troop troopEntity) {
                Troop otherTroop = troopEntity;
                if (otherTroop != this && canDealDamage) {
                    System.out.println("Damage can be dealt. Starting cooldown.");
                    applyDamage(otherTroop);  // Pas schade toe en start cooldown
                }
            }
        }
    }

    private void applyDamage(Troop otherTroop) {
        // Stop de beweging tijdelijk
        setSpeed(0);

        if (damageTask != null) {
            damageTask.cancel();  // Stop de vorige taak als deze nog loopt
        }

        damageTask = new TimerTask() {
            @Override
            public void run() {
                if (isAlive() && otherTroop.isAlive()) {
                    System.out.println("Applying damage. This troop HP: " + hp + ", Enemy HP: " + otherTroop.hp);
                    otherTroop.takeDamage(10);  // Schade aan de vijand
                }
                canDealDamage = true;
                System.out.println("Damage cooldown complete. Can deal damage again.");
            }
        };

        damageCooldownTimer.schedule(damageTask, 3000);  // 3 seconden cooldown
        canDealDamage = false;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        System.out.println("Took damage: " + damage + ". Remaining HP: " + hp);
        if (hp <= 0) {
            System.out.println("Troop died.");
            remove();  // Verwijder entiteit

            if (enemy != null && enemy.isAlive()) {
                enemy.continueWalking();
            }

            if (damageCooldownTimer != null) {
                damageCooldownTimer.cancel();  // Stop de timer als de entiteit wordt verwijderd
            }
        }
    }

    public void continueWalking() {
        setMotion(speed, 90d);
        System.out.println("Continuing to walk after enemy defeat.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Coordinate2D getLocation() {
        Coordinate2D currentLocation = getLocationInScene();
        return currentLocation.add(new Coordinate2D(getWidth() / 2, getHeight() / 3));
    }

    @Override
    public void notifyBoundaryTouching(SceneBorder border){
        switch(border){
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

    // Specifieke troepen moeten hun eigen aanvalsmethode definiëren
    public abstract void attack();
}
