package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.StaticScene;

import javafx.scene.paint.Color;

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
            Logo logo = new Logo(new Coordinate2D(getWidth() / 2, getHeight() / 3));
            logo.setAnchorPoint(AnchorPoint.CENTER_CENTER);
            addEntity(logo);

            Button startButton = new Button(
                    new Coordinate2D(getWidth() / 2, getHeight() / 1.5),
                    "Play",
                    action -> ageOfWar.setActiveScene(1)
            );
            addEntity(startButton);
        } else {
            TextEntity mainText = new TextEntity(
                    new Coordinate2D(getWidth() / 2, getHeight() / 2),
                    sceneText
            );

            FontUtils.setButtonFont(mainText, 80, Color.WHITE);
            addEntity(mainText);

            if ("Victory!".equals(sceneText) || "Defeat!".equals(sceneText)) {
                Button playAgainButton = new Button(
                        new Coordinate2D(getWidth() / 4, getHeight() / 1.5),
                        "Play Again?",
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