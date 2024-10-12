package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Timer;
import java.util.TimerTask;

public class ErrorText extends TextEntity {
    private static final int ERROR_DURATION = 2000;

    public ErrorText(Coordinate2D initialLocation) {
        super(initialLocation);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
        setFill(Color.RED);
    }

    public void errorNotEnoughCredits() {
        setText("Not enough credits!");
        resetText();
    }

    private void resetText() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setText("");
            }
        }, ERROR_DURATION);
    }
}
