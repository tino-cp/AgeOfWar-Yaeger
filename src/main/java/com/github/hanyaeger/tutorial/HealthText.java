package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class HealthText extends DynamicTextEntity {
    private Troop troop;

    public HealthText(Troop troop) {
        super(new Coordinate2D(-100, -100), String.valueOf(troop.getHp()));
        this.troop = troop;
        setFill(Color.RED);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
    }

    public void updateHealthText() {
        setText(String.valueOf(troop.getHp()));
    }

    public void updateHealthTextLocation() {
        // Delay timer omdat de breedte van de troop niet gelijk bekend is
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Text tempText = new Text(getText());
                tempText.setFont(getFont());
                double textWidth = tempText.getLayoutBounds().getWidth();

                setAnchorLocation(new Coordinate2D(troop.getAnchorLocation().getX() + troop.getWidth() / 2 - textWidth / 2, troop.getAnchorLocation().getY() - 25));
            }
        }, 50);
    }
}
