package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;

import javafx.scene.paint.Color;

public class CreditText extends TextEntity {
    private double credit = 100000;

    public CreditText(Coordinate2D initialLocation) {
        super(initialLocation);

        FontUtils.setFont(this, 20, Color.YELLOW);
        setCreditText(credit);
    }

    public void setCreditText(double credit){
        setText("$ " + credit);
    }

    public void decreaseCredit(double amount){
        credit -= amount;
        setCreditText(credit);
    }

    public void increaseCredit(double amount){
        credit += amount;
        setCreditText(credit);
    }

    public double getCredit(){
        return credit;
    }
}
