package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseEnterListener;
import com.github.hanyaeger.api.userinput.MouseExitListener;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;


public class SpawnTroopButton extends SpriteEntity implements MouseButtonPressedListener, MouseEnterListener, MouseExitListener {
    private int troopType;
    private MainScene mainScene;

    public SpawnTroopButton(Coordinate2D location, String normalImage, int troopType, MainScene mainScene) {
        super(normalImage, location);

        this.troopType = troopType;
        this.mainScene = mainScene;
    }

    @Override
    public void onMouseButtonPressed(MouseButton mouseButton, Coordinate2D coordinate2D) {
        if (mouseButton == MouseButton.PRIMARY) {
            mainScene.spawnTroop(troopType);
        }
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