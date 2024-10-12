package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.StaticScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Scene extends StaticScene {
    private AgeOfWar ageOfWar;
    private String sceneText;

    public Scene(AgeOfWar ageOfWar, String sceneText) {
        this.ageOfWar = ageOfWar;
        this.sceneText = sceneText;
    }

    @Override
    public void setupScene() {
        setBackgroundImage("backgrounds/background.png");
    }

    @Override
    public void setupEntities() {
        TextEntity mainText = new TextEntity(
                new Coordinate2D(getWidth() / 2, getHeight() / 2),
                sceneText
        );

        mainText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        mainText.setFill(Color.DARKBLUE);
        mainText.setFont(Font.font("Roboto", FontWeight.SEMI_BOLD, 80));
        addEntity(mainText);

        switch (sceneText) {
            case "Age Of War":
                Button startButton = new Button(
                        new Coordinate2D(getWidth() / 2, getHeight() / 1.5),
                        "Start Game",
                        action -> ageOfWar.setActiveScene(1)
                );
                addEntity(startButton);
                break;
            case "Game Over", "You Won":
                Button playAgainButton = new Button(
                        new Coordinate2D(getWidth() / 4, getHeight() / 1.5),
                        "Play Again",
                        action -> ageOfWar.setActiveScene(1)
                );
                addEntity(playAgainButton);

                Button quitButton = new Button(
                        new Coordinate2D(getWidth() / 4 * 3, getHeight() / 1.5),
                        "Exit Game",
                        action -> ageOfWar.quit()
                );
                addEntity(quitButton);
                break;
            default:
                break;
        }
    }
}