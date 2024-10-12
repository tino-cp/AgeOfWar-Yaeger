package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Timer;
import java.util.TimerTask;

public class HealthText extends DynamicTextEntity {
    private Troop troop;

    public HealthText(Troop troop) {
        super(new Coordinate2D(-100, -100), String.valueOf(troop.getHp()));
        this.troop = troop;

        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
        setFill(Color.RED);
    }

    public void updateHealthText() {
        setText(String.valueOf(troop.getHp()));
    }

    public void updateHealthTextLocation() {
        // Delay timer omdat de breedte van de troop niet gelijk bekend is
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setAnchorLocation(new Coordinate2D(troop.getAnchorLocation().getX() + troop.getWidth() / 2, troop.getAnchorLocation().getY() - 15));
            }
        }, 50);
    }
}
