package com.github.hanyaeger.ageofwar.entities.scenes;

import com.github.hanyaeger.ageofwar.AgeOfWar;
import com.github.hanyaeger.ageofwar.entities.Base;
import com.github.hanyaeger.ageofwar.entities.Floor;
import com.github.hanyaeger.ageofwar.entities.texts.HealthText;
import com.github.hanyaeger.ageofwar.entities.abilities.AbilityButton;
import com.github.hanyaeger.ageofwar.entities.buttons.SpawnTroopButton;
import com.github.hanyaeger.ageofwar.entities.abilities.AbilitySpawner;
import com.github.hanyaeger.ageofwar.entities.texts.CreditText;
import com.github.hanyaeger.ageofwar.entities.texts.ErrorText;
import com.github.hanyaeger.ageofwar.entities.troops.Artillery;
import com.github.hanyaeger.ageofwar.entities.troops.Cavalry;
import com.github.hanyaeger.ageofwar.entities.troops.Infantry;
import com.github.hanyaeger.ageofwar.entities.troops.Troop;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.EntitySpawnerContainer;
import com.github.hanyaeger.api.scenes.DynamicScene;

import java.util.*;

public class MainScene extends DynamicScene implements EntitySpawnerContainer {
    private AgeOfWar ageOfWar;
    private Timer enemySpawnTimer;

    private CreditText creditText;
    private ErrorText errorText;

    public List<Troop> troopList = new ArrayList<>();
    public List<Troop> enemyList = new ArrayList<>();

    private boolean canTroopsMove = true;
    private boolean canEnemiesMove = true;

    private static final int MAX_TROOP_COUNT = 10;
    private static final double SPAWN_X_LIMIT = Base.getBaseWidth() / 2;
    private static final double ENEMY_SPAWN_X_LIMIT = AgeOfWar.getGameWidth() - Base.getBaseWidth() / 2;

    private AbilitySpawner abilitySpawner;

    private Random random = new Random();

    public MainScene(AgeOfWar ageOfWar) {
        this.ageOfWar = ageOfWar;
        enemySpawnTimer = new Timer();
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
        setupFloor();
        setupAbilityButton();
        setupBases();
        setupEnemySpawnOnTimer();
    }

    @Override
    public void setupEntitySpawners() {
        addEntitySpawner(abilitySpawner);
    }

    public void setupHealthDisplay(HealthText healthText) {
        addEntity(healthText);
    }

    private void setupBases() {
        addEntity(new Base("sprites/base-friendly.png", new Coordinate2D(0, Floor.getFloorY() - (Base.getBaseHeight() / 9) * 8), true, this, ageOfWar));
        addEntity(new Base("sprites/base-enemy.png", new Coordinate2D(getWidth() - Base.getBaseWidth(), Floor.getFloorY() - (Base.getBaseHeight() / 9) * 8), false, this, ageOfWar));
    }

    private void setupAbilityButton() {
        addEntity(new AbilityButton(new Coordinate2D(350, 50), abilitySpawner, "sprites/ability-icon.png", this));
    }

    private void setupSpawnButtons() {
        addEntity(new SpawnTroopButton(new Coordinate2D(50, 50), "sprites/infantry-icon.png", 0, this));
        addEntity(new SpawnTroopButton(new Coordinate2D(100, 50), "sprites/artillery-icon.png", 1, this));
        addEntity(new SpawnTroopButton(new Coordinate2D(150, 50), "sprites/cavalry-icon.png", 2, this));
    }

    private void setupFloor() {
        addEntity(new Floor(new Coordinate2D(0, Floor.getFloorY()), this));
    }

    public void setupTextDisplay() {
        creditText = new CreditText(new Coordinate2D(100, 20));
        addEntity(creditText);

        errorText = new ErrorText(new Coordinate2D(300, 20));
        addEntity(errorText);
    }

    private void setupEnemySpawnOnTimer() {
        enemySpawnTimer.schedule(createNewEnemySpawnTask(), 2000);
    }

    private TimerTask createNewEnemySpawnTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (canSpawnEnemy()) {
                    Troop newEnemy = spawnRandomEnemy();
                    addEntity(newEnemy);
                    enemyList.add(newEnemy);
                }

                long nextDelay = getRandomSpawnDelay();
                enemySpawnTimer.schedule(createNewEnemySpawnTask(), nextDelay);
            }
        };
    }

    private boolean canSpawnEnemy() {
        return enemyList.size() < MAX_TROOP_COUNT && !isEnemyTroopInSpawnZone();
    }

    private boolean isEnemyTroopInSpawnZone() {
        return getEnemySpawnZoneTroops().stream().anyMatch(troop -> troop.getAnchorLocation().getX() > ENEMY_SPAWN_X_LIMIT);
    }

    private long getRandomSpawnDelay() {
        return 3000 + random.nextLong(2000);
    }

    private Troop spawnRandomEnemy() {
        double chance = random.nextDouble() * 100;
        Coordinate2D location = new Coordinate2D(getWidth() - Troop.getTroopWidth(), Floor.getFloorY() - Troop.getTroopHeight());

        if (chance <= 15) {
            Coordinate2D cavalryLocation = new Coordinate2D(getWidth() - Cavalry.getCavalryWidth(), Floor.getFloorY() - Cavalry.getCavalryHeight());
            return new Cavalry(cavalryLocation, "sprites/cavalry-enemy.png", 1, this);
        } else if (chance <= 50) {
            return new Artillery(location, "sprites/artillery-enemy.png", 1, this);
        } else {
            return new Infantry(location, "sprites/infantry-enemy.png", 1, this);
        }
    }

    private List<Troop> getEnemySpawnZoneTroops() {
        List<Troop> baseLineTroops = new ArrayList<>();
        double spawnZoneLimit = SPAWN_X_LIMIT;

        // Vind alle enemies die zich in de spawnzone bevinden
        for (Troop enemy : enemyList) {
            if (enemy.getAnchorLocation().getX() > spawnZoneLimit) {
                baseLineTroops.add(enemy);
            }
        }
        return baseLineTroops;
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
        if (troopList.size() >= MAX_TROOP_COUNT || isTroopInSpawnZone()) {
            return;
        }

        double requiredCredits = getTroopCreditCost(troopType);

        if (creditText.getCredit() >= requiredCredits) {
            Troop newTroop = createTroop(troopType);
            creditText.decreaseCredit(requiredCredits);
            troopList.add(newTroop);
            addEntity(newTroop);
        } else {
            errorText.displayErrorNotEnoughCredits();
        }
    }

    private boolean isTroopInSpawnZone() {
        return getSpawnZoneTroops().stream().anyMatch(troop -> troop.getAnchorLocation().getX() < SPAWN_X_LIMIT);
    }

    private Troop createTroop(int troopType) {
        Coordinate2D location = new Coordinate2D(0, Floor.getFloorY() - Troop.getTroopHeight());
        switch (troopType) {
            case 0:
                return new Infantry(location, "sprites/infantry.png", 0, this);
            case 1:
                return new Artillery(location, "sprites/artillery.png", 0, this);
            case 2:
                Coordinate2D cavalryLocation = new Coordinate2D(0, Floor.getFloorY() - Cavalry.getCavalryHeight());
                return new Cavalry(cavalryLocation, "sprites/cavalry.png", 0, this);
            default:
                return null;
        }
    }

    private double getTroopCreditCost(int troopType) {
        return switch (troopType) {
            case 0 -> Infantry.getCreditCostStatic();
            case 1 -> Artillery.getCreditCostStatic();
            case 2 -> Cavalry.getCreditCostStatic();
            default -> 0;
        };
    }

    public CreditText getCreditText() {
        return creditText;
    }

    public ErrorText getErrorText() {
        return errorText;
    }

    private List<Troop> getSpawnZoneTroops() {
        List<Troop> baseLineTroops = new ArrayList<>();
        double spawnZoneLimit = SPAWN_X_LIMIT;

        // Vind alle troepen die zich in de spawnzone bevinden
        for (Troop troop : troopList) {
            if (troop.getAnchorLocation().getX() < spawnZoneLimit) {
                baseLineTroops.add(troop);
            }
        }
        return baseLineTroops;
    }
}
