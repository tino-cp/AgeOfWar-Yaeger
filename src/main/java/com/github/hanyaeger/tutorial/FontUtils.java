package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FontUtils {
    private FontUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static void setButtonFont(TextEntity text, double size, Color color) {
        text.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        text.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, size));
        text.setFill(color);
    }

    public static void setFont(TextEntity text, double size, Color color) {
        text.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        text.setFont(Font.font("Arial", FontWeight.NORMAL, size));
        text.setFill(color);
    }
}
