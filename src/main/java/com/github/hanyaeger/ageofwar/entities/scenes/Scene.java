package com.github.hanyaeger.ageofwar.entities.scenes;

import com.github.hanyaeger.ageofwar.AgeOfWar;
import com.github.hanyaeger.ageofwar.entities.Logo;
import com.github.hanyaeger.ageofwar.entities.buttons.Button;
import com.github.hanyaeger.ageofwar.utils.FontUtils;
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
        if (isMainMenuScene()) {
            setupMainMenu();
        } else {
            setupEndScreen();
        }
    }

    // Main menu (Start scene)
    private boolean isMainMenuScene() {
        return "Age Of War".equals(sceneText);
    }

    private void setupMainMenu() {
        Logo logo = createLogo();
        addEntity(logo);

        Button startButton = createStartButton();
        addEntity(startButton);
    }

    private Logo createLogo() {
        Logo logo = new Logo(new Coordinate2D(getWidth() / 2, getHeight() / 3));
        logo.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        return logo;
    }

    private Button createStartButton() {
        return new Button(new Coordinate2D(getWidth() / 2, getHeight() / 1.5), "Play", action -> ageOfWar.setActiveScene(1));
    }

    // End scene
    private void setupEndScreen() {
        TextEntity mainText = createMainText();
        addEntity(mainText);

        if (isVictoryOrDefeat()) {
            setupEndSceneButtons();
        }
    }

    private TextEntity createMainText() {
        TextEntity mainText = new TextEntity(new Coordinate2D(getWidth() / 2, getHeight() / 2), sceneText);
        FontUtils.setButtonFont(mainText, 80, Color.WHITE);
        return mainText;
    }

    private boolean isVictoryOrDefeat() {
        return "Victory!".equals(sceneText) || "Defeat!".equals(sceneText);
    }

    private void setupEndSceneButtons() {
        Button playAgainButton = createPlayAgainButton();
        addEntity(playAgainButton);

        Button quitButton = createQuitButton();
        addEntity(quitButton);
    }

    private Button createPlayAgainButton() {
        return new Button(new Coordinate2D(getWidth() / 4, getHeight() / 1.5), "Play Again?",
                action -> {
                    ageOfWar.addScene(1, new MainScene(ageOfWar));
                    ageOfWar.setActiveScene(1);
                }
        );
    }

    private Button createQuitButton() {
        return new Button(new Coordinate2D(getWidth() / 4 * 3, getHeight() / 1.5), "Exit Game", action -> ageOfWar.quit());
    }
}
