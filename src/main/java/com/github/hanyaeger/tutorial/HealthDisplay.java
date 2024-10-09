package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HealthDisplay extends DynamicTextEntity {
    private Troop troop;

    public HealthDisplay(Troop troop) {
        super(new Coordinate2D(troop.getAnchorLocation().getX(), troop.getAnchorLocation().getY() - 20), String.valueOf(troop.getHp()));
        this.troop = troop;
        setFill(Color.RED);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
    }

    public void updateHealth() {
        setText(String.valueOf(troop.getHp()));
    }

    public void updatePosition() {
        setAnchorLocation(new Coordinate2D(troop.getAnchorLocation().getX(), troop.getAnchorLocation().getY() - 20));
    }
}
