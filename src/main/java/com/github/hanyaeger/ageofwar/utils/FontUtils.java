package com.github.hanyaeger.ageofwar.utils;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class FontUtils {
    private static final String DEFAULT_FONT = "Arial";
    private static final String BUTTON_FONT = "Bookman Old Style";

    // Private constructor om te voorkomen dat de klasse wordt geïnstantieerd
    private FontUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static void setButtonFont(TextEntity text, double size, Color color) {
        setFont(text, BUTTON_FONT, FontWeight.BOLD, size, color);
    }

    public static void setFont(TextEntity text, double size, Color color) {
        setFont(text, DEFAULT_FONT, FontWeight.NORMAL, size, color);
    }

    private static void setFont(TextEntity text, String fontName, FontWeight weight, double size, Color color) {
        text.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        text.setFont(Font.font(fontName, weight, size));
        text.setFill(color);
    }
}
