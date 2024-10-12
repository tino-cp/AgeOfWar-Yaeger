package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CreditText extends TextEntity {
    private double credit = 1000;

    public CreditText(Coordinate2D initialLocation) {
        super(initialLocation);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
        setFill(Color.LIGHTGOLDENRODYELLOW);
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
