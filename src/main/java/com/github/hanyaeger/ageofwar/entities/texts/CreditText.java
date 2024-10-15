package com.github.hanyaeger.ageofwar.entities.texts;

import com.github.hanyaeger.ageofwar.utils.FontUtils;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;

import javafx.scene.paint.Color;

public class CreditText extends TextEntity {
    private double credit = 10000;

    public CreditText(Coordinate2D initialLocation) {
        super(initialLocation);
        initializeText();
    }

    private void initializeText() {
        FontUtils.setFont(this, 20, Color.YELLOW);
        updateCreditText();
    }

    public double getCredit(){
        return credit;
    }

    public void decreaseCredit(double amount){
        credit -= amount;
        updateCreditText();
    }

    public void increaseCredit(double amount){
        credit += amount;
        updateCreditText();
    }

    private void updateCreditText() {
        setText(formatCredit(credit));
    }

    private String formatCredit(double credit) {
        return "$ " + credit;
    }
}
