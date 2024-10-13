package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.scenes.DynamicScene;

import java.util.*;

public class MainScene extends DynamicScene implements EntitySpawnerContainer {
    private AgeOfWar ageOfWar;

    private CreditText creditText;
    private ErrorText errorText;

    private Queue<Troop> troopQueue = new LinkedList<>();
    public List<Troop> troopList = new ArrayList<>();
    public List<Troop> enemyList = new ArrayList<>();

    private boolean canTroopsMove = true;
    private boolean canEnemiesMove = true;

    private static final int MAX_TROOP_COUNT = 10;
    private static final int SPAWN_X_LIMIT = 100;

    private AbilitySpawner abilitySpawner;

    public MainScene(AgeOfWar ageOfWar) {
        this.ageOfWar = ageOfWar;
    }

    @Override
    public void setupScene() {
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
        AbilityButton abilityButton = new AbilityButton(new Coordinate2D(350, 50), abilitySpawner, "sprites/ability-icon.png", this);
        addEntity(abilityButton);
    }

    private void setupSpawnButtons() {
        SpawnTroopButton spawnTroopButton = new SpawnTroopButton(new Coordinate2D(50, 50), "sprites/infantry-icon.png", 0, this);
        SpawnTroopButton spawnArtilleryButton = new SpawnTroopButton(new Coordinate2D(150, 50), "sprites/artillery-icon.png", 1, this);
        SpawnTroopButton spawnCavalryButton = new SpawnTroopButton(new Coordinate2D(250, 50), "sprites/cavalry-icon.png", 2, this);

        addEntity(spawnTroopButton);
        addEntity(spawnArtilleryButton);
        addEntity(spawnCavalryButton);
    }

    private void setupEnemySpawnOnTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Troop newEnemy = new Infantry(new Coordinate2D(1450, getHeight() - 100), "sprites/enemy.png", 1, MainScene.this, ageOfWar);
                addEntity(newEnemy);
                enemyList.add(newEnemy);
            }
        }, 1000, 12000);
    }

    private void setupFloor() {
        Floor floor = new Floor(new Coordinate2D(0, 450), this);
        addEntity(floor);
    }

    public void setupHealthDisplay(HealthText healthText) {
        addEntity(healthText);
    }

    public void setupTextDisplay() {
        creditText = new CreditText(new Coordinate2D(100, 20));
        addEntity(creditText);

        errorText = new ErrorText(new Coordinate2D(300, 20));
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

        double requiredCredits = getTroopCreditCost(troopType);

        if (creditText.getCredit() >= requiredCredits) {
            Troop newTroop = createTroop(troopType);

            creditText.decreaseCredit(requiredCredits);
            troopQueue.add(newTroop);
            troopList.add(newTroop);
            addEntity(newTroop);
        } else {
            errorText.errorNotEnoughCredits();
        }
    }

    private Troop createTroop(int troopType) {
        Coordinate2D location = new Coordinate2D(0, getHeight() - 100);
        switch (troopType) {
            case 0:
                return new Infantry(location, "sprites/infantry.png", 0, this, ageOfWar);
            case 1:
                return new Artillery(location, "sprites/artillery.png", 0, this, ageOfWar);
            case 2:
                return new Cavalry(location, "sprites/cavalry.png", 0, this, ageOfWar);
            default:
                return null;
        }
    }

    private double getTroopCreditCost(int troopType) {
        switch (troopType) {
            case 0:
                return Infantry.getCreditCostStatic();
            case 1:
                return Artillery.getCreditCostStatic();
            case 2:
                return Cavalry.getCreditCostStatic();
            default:
                return 0;
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
