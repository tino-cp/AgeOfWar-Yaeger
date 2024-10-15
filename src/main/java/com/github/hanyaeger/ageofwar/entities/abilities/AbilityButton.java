package com.github.hanyaeger.ageofwar.entities.abilities;

import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseEnterListener;
import com.github.hanyaeger.api.userinput.MouseExitListener;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;

import java.util.Timer;
import java.util.TimerTask;

public class AbilityButton extends SpriteEntity implements MouseButtonPressedListener, MouseEnterListener, MouseExitListener {
    private static final long ABILITY_DURATION = 5000;
    private static final long COOLDOWN_TIME = 10000;
    private static final double OPACITY_DISABLED = 0.5;
    private static final double OPACITY_DEFAULT = 1;
    private static final double CREDIT_COST = 5000;

    private final MainScene mainScene;
    private final AbilitySpawner abilitySpawner;

    private boolean isOnCooldown = false;

    public AbilityButton(Coordinate2D location, AbilitySpawner abilitySpawner, String normalImage, MainScene mainScene) {
        super(normalImage, location);
        this.abilitySpawner = abilitySpawner;
        this.mainScene = mainScene;
    }

    @Override
    public void onMouseButtonPressed(MouseButton mouseButton, Coordinate2D coordinate2D) {
        if (mouseButton == MouseButton.PRIMARY && !isOnCooldown) {
            if (mainScene.getCreditText().getCredit() >= CREDIT_COST) {
                mainScene.getCreditText().decreaseCredit(CREDIT_COST);
                activateAbility();
                startAbilityCooldown();
            } else {
                mainScene.getErrorText().displayErrorNotEnoughCredits();
            }
        }
    }

    private void activateAbility() {
        abilitySpawner.resume();
        scheduleAbilityDeactivation();
    }

    private void deactivateAbility() {
        abilitySpawner.pause();
    }

    private void scheduleAbilityDeactivation() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                deactivateAbility();
            }
        }, ABILITY_DURATION);
    }

    private void startAbilityCooldown() {
        isOnCooldown = true;
        setOpacity(OPACITY_DISABLED);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resetCooldown();
            }
        }, COOLDOWN_TIME);
    }

    private void resetCooldown() {
        isOnCooldown = false;
        setOpacity(OPACITY_DEFAULT);
    }

    @Override
    public void onMouseEntered() {
        setCursor(Cursor.HAND);
    }

    @Override
    public void onMouseExited() {
        setCursor(Cursor.DEFAULT);
    }
}
