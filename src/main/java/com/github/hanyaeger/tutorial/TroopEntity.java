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

public class TroopEntity extends DynamicSpriteEntity implements Collider, Collided, SceneBorderTouchingWatcher {
    private int hp;
    private double speed;
    private boolean canDealDamage = true;
    private TroopEntity enemy;
    private Timer damageCooldownTimer;
    private TimerTask damageTask;

    public TroopEntity(Coordinate2D location, String sprite, int hp, double speed) {
        super(sprite, location);
        this.hp = hp;
        this.speed = speed;
        setMotion(speed, 90d);  // Beweeg in een rechte lijn
        initializeDamageCooldownTimer();  // Initialize cooldown timer
    }

    public void setEnemy(TroopEntity enemy) {
        this.enemy = enemy;
    }

    public void setCanDealDamage(boolean canDealDamage) {
        this.canDealDamage = canDealDamage;
    }

    private void initializeDamageCooldownTimer() {
        damageCooldownTimer = new Timer();
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof TroopEntity troopEntity) {
                TroopEntity otherTroop = troopEntity;
                if (otherTroop != this && canDealDamage) {
                    System.out.println("Damage can be dealt. Starting cooldown.");
                    applyDamage(otherTroop);  // Pas schade toe en start cooldown
                }
            }
        }
    }

    private void applyDamage(TroopEntity otherTroop) {
        // Stop de beweging tijdelijk
        setSpeed(0);

        // Zorg ervoor dat de schade alleen eenmaal per cooldown-periode wordt toegepast
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
                // Na het toepassen van schade, wordt cooldown opnieuw ingesteld
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
        // Als de vijand is verslagen, blijf doorlopen
        setMotion(speed, 90d);
        System.out.println("Continuing to walk after enemy defeat.");
    }

    public boolean isAlive() {
        return hp > 0;
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
}
