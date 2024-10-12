package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.scenes.DynamicScene;

import java.util.*;

public class MainScene extends DynamicScene implements EntitySpawnerContainer {

    private SpawnTroopButton spawnTroopButton, spawnArtilleryButton, spawnCavalryButton;
    private CreditText creditText;
    private ErrorText errorText;
    private Queue<Troop> troopQueue = new LinkedList<>();
    public List<Troop> troopList = new ArrayList<>();
    public List<Troop> enemyList = new ArrayList<>();
    private boolean canTroopsMove = true;
    private boolean canEnemiesMove = true;

    private static final int MAX_TROOP_COUNT = 10;
    private static final int SPAWN_X_LIMIT = 100;

    private ProjectileSpawner projectileSpawner;
    private AbilitySpawner abilitySpawner;
    private AbilityButton abilityButton;

    public MainScene(AgeOfWar ageOfWar) {
    }

    @Override
    public void setupScene() {
        //setBackgroundAudio("audio/AgeOfWar_ThemeSong.mp3");
        setBackgroundImage("backgrounds/background.png");
        abilitySpawner = new AbilitySpawner(this);
    }

    @Override
    public void setupEntities() {
        setupSpawnButtons();
        setupTextDisplay();
        setupEnemySpawnOnTimer();
        setupFloor();
        setupAbilityButton();
    }

    @Override
    public void setupEntitySpawners() {
        addEntitySpawner(abilitySpawner);
    }

    private void setupAbilityButton() {
        abilityButton = new AbilityButton(new Coordinate2D(350, 50), abilitySpawner, "sprites/ability-icon.png", this);
        addEntity(abilityButton);
    }

    private void setupSpawnButtons() {
        spawnTroopButton = new SpawnTroopButton(new Coordinate2D(50, 50), "sprites/infantry-icon.png", 0, this);
        spawnArtilleryButton = new SpawnTroopButton(new Coordinate2D(150, 50), "sprites/artillery-icon.png", 1, this);
        spawnCavalryButton = new SpawnTroopButton(new Coordinate2D(250, 50), "sprites/cavalry-icon.png", 2, this);

        addEntity(spawnTroopButton);
        addEntity(spawnArtilleryButton);
        addEntity(spawnCavalryButton);
    }

    private void setupEnemySpawnOnTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Troop newEnemy = new Infantry(new Coordinate2D(1450, getHeight() - 100), "sprites/enemy.png", 1, MainScene.this);
                addEntity(newEnemy);
                enemyList.add(newEnemy);
            }
        }, 1000, 4000);
    }

    private void setupFloor() {
        FloorEntity floor = new FloorEntity(new Coordinate2D(0, 450), this);
        addEntity(floor);
    }

    public void setupHealthDisplay(HealthText healthText) {
        addEntity(healthText);
    }

    public void setupTextDisplay() {
        creditText = new CreditText(new Coordinate2D(50, 10));
        addEntity(creditText);

        errorText = new ErrorText(new Coordinate2D(200, 10));
        addEntity(errorText);
    }

    public void setCanTroopsMove(boolean canMove) {
        this.canTroopsMove = canMove;
        if (canMove) continueWalkingTroops();
    }

    public boolean canTroopsMove() {
        return canTroopsMove;
    }

    public void continueWalkingTroops() {
        troopList.forEach(Troop::updateMovement);
    }

    public void setCanEnemiesMove(boolean canMove) {
        this.canEnemiesMove = canMove;
        if (canMove) continueWalkingEnemies();
    }

    public boolean canEnemiesMove() {
        return canEnemiesMove;
    }

    public void continueWalkingEnemies() {
        enemyList.forEach(Troop::updateMovement);
    }

    public void spawnTroop(int troopType) {
        if (troopQueue.size() >= MAX_TROOP_COUNT || getFrontLineTroops().stream().anyMatch(troop -> troop.getAnchorLocation().getX() < SPAWN_X_LIMIT)) {
            return;
        }

        Troop newTroop = createTroop(troopType);
        if (newTroop != null) {
            creditText.decreaseCredit(newTroop.getCreditCost());
            troopQueue.add(newTroop);
            troopList.add(newTroop);
            addEntity(newTroop);
        }
    }

    private Troop createTroop(int troopType) {
        Coordinate2D location = new Coordinate2D(0, getHeight() - 100);
        switch (troopType) {
            case 0:
                return new Infantry(location, "sprites/infantry.png", 0, this);
            case 1:
                return new Artillery(location, "sprites/artillery.png", 0, this);
            case 2:
                return new Cavalry(location, "sprites/cavalry.png", 0, this);
            default:
                return null;
        }
    }

    public CreditText getCreditText() {
        return creditText;
    }

    public ErrorText getErrorText() {
        return errorText;
    }

    private List<Troop> getFrontLineTroops() {
        List<Troop> frontLineTroops = new ArrayList<>();
        double minX = Integer.MAX_VALUE;
        for (Troop troop : troopQueue) {
            if (troop.getAnchorLocation().getX() < minX) {
                minX = troop.getAnchorLocation().getX();
            }
        }
        for (Troop troop : troopQueue) {
            if (troop.getAnchorLocation().getX() == minX) {
                frontLineTroops.add(troop);
            }
        }
        return frontLineTroops;
    }
}
