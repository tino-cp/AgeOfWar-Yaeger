package com.github.hanyaeger.ageofwar.entities.buttons;

import com.github.hanyaeger.ageofwar.utils.FontUtils;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseEnterListener;
import com.github.hanyaeger.api.userinput.MouseExitListener;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class Button extends TextEntity implements MouseButtonPressedListener, MouseEnterListener, MouseExitListener {
    private Consumer<Void> action;

    public Button(Coordinate2D initialLocation, String text, Consumer<Void> action) {
        super(initialLocation, text);
        this.action = action;

        FontUtils.setButtonFont(this, 30, Color.WHITE);
    }

    @Override
    public void onMouseEntered(){
        setFill(Color.LIGHTGRAY);
        setCursor(Cursor.HAND);
    }

    @Override
    public void onMouseExited(){
        setFill(Color.WHITE);
        setCursor(Cursor.DEFAULT);
    }


    @Override
    public void onMouseButtonPressed(MouseButton mouseButton, Coordinate2D coordinate2D) {
        if (mouseButton == MouseButton.PRIMARY) {
            triggerAction();
        }
    }

    private void triggerAction() {
        action.accept(null);
    }
}
