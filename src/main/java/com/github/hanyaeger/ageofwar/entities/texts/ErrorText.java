package com.github.hanyaeger.ageofwar.entities.texts;

import com.github.hanyaeger.ageofwar.utils.FontUtils;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;

import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

public class ErrorText extends TextEntity {
    private static final int ERROR_DURATION = 2000;
    private static final String ERROR_MESSAGE = "Not enough credits!";
    private Timer timer;

    public ErrorText(Coordinate2D initialLocation) {
        super(initialLocation);
        FontUtils.setFont(this, 20, Color.RED);
    }

    public void displayErrorNotEnoughCredits() {
        setText(ERROR_MESSAGE);
        scheduleTextReset();
    }

    private void scheduleTextReset() {
        // Cancel de bestaande timer om te voorkomen dat er meerdere keren wordt gereset
        cancelExistingTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetText();
            }
        }, ERROR_DURATION);
    }

    private void resetText() {
        setText("");
    }

    private void cancelExistingTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
