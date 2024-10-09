package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HealthText extends DynamicTextEntity {
    private Troop troop;

    public HealthText(Troop troop) {
        super(new Coordinate2D(0, 0), String.valueOf(troop.getHp()));
        this.troop = troop;
        setFill(Color.RED);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));

        Text health = new Text(String.valueOf(troop.getHp()));
        health.setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
        double textWidth = health.getLayoutBounds().getWidth();

        setAnchorLocation(new Coordinate2D(troop.getAnchorLocation().getX() + troop.getWidth() / 2 + textWidth / 2, troop.getAnchorLocation().getY() - 25));
    }

    public void updateHealthDisplay() {
        setText(String.valueOf(troop.getHp()));
    }
}
