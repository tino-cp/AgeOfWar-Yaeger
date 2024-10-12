package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
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
        if ("Age Of War".equals(sceneText)) {
            LogoEntity logo = new LogoEntity(new Coordinate2D(getWidth() / 2, getHeight() / 3));
            logo.setAnchorPoint(AnchorPoint.CENTER_CENTER);
            addEntity(logo);

            Button startButton = new Button(
                    new Coordinate2D(getWidth() / 2, getHeight() / 1.5),
                    "Start Game",
                    action -> ageOfWar.setActiveScene(1)
            );
            addEntity(startButton);
        } else {
            TextEntity mainText = new TextEntity(
                    new Coordinate2D(getWidth() / 2, getHeight() / 2),
                    sceneText
            );

            mainText.setAnchorPoint(AnchorPoint.CENTER_CENTER);
            mainText.setFill(Color.DARKBLUE);
            mainText.setFont(Font.font("Roboto", FontWeight.SEMI_BOLD, 80));
            addEntity(mainText);

            if ("Game Over".equals(sceneText) || "You Won".equals(sceneText)) {
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
            }
        }
    }
}