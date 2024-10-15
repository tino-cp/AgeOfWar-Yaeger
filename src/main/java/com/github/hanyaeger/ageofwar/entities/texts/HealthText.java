package com.github.hanyaeger.ageofwar.entities.texts;

import com.github.hanyaeger.ageofwar.entities.Base;
import com.github.hanyaeger.ageofwar.entities.Damageable;
import com.github.hanyaeger.ageofwar.entities.troops.Cavalry;
import com.github.hanyaeger.ageofwar.entities.troops.Troop;
import com.github.hanyaeger.ageofwar.utils.FontUtils;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;

public class HealthText extends DynamicTextEntity {
    private final Damageable entity;
    private final double widthOffset;
    private final double heightOffset;

    public HealthText(Damageable entity) {
        super(new Coordinate2D(-100, -100), String.valueOf(getEntityHealth(entity)));
        this.entity = entity;

        if (entity instanceof Troop troop) {
            this.widthOffset = getTroopWidth(troop) / 2;
            this.heightOffset = -15;
            FontUtils.setFont(this, 20, Color.RED);
        } else if (entity instanceof Base) {
            this.widthOffset = Base.getBaseWidth() / 2;
            this.heightOffset = -20;
            FontUtils.setFont(this, 25, Color.RED);
        } else {
            this.widthOffset = 0;
            this.heightOffset = 0;
        }
    }

    public void initialize() {
        updateHealthText();
        updateHealthTextLocation();
    }

    public void updateHealthText() {
        setText(String.valueOf(getEntityHealth(entity)));
    }

    public void updateHealthTextLocation() {
        Coordinate2D entityLocation = getEntityLocation();
        if (entityLocation != null) {
            setAnchorLocation(new Coordinate2D(entityLocation.getX() + widthOffset, entityLocation.getY() + heightOffset));
        }
    }

    private static int getEntityHealth(Damageable entity) {
        if (entity instanceof Troop troop) {
            return troop.getHp();
        } else if (entity instanceof Base base) {
            return base.getHp();
        }
        return 0;
    }

    private static double getTroopWidth(Troop troop) {
        return troop instanceof Cavalry ? Cavalry.getCavalryWidth() : Troop.getTroopWidth();
    }

    private Coordinate2D getEntityLocation() {
        if (entity instanceof Troop troop) {
            return troop.getAnchorLocation();
        } else if (entity instanceof Base base) {
            return base.getAnchorLocation();
        }
        return null;
    }
}
