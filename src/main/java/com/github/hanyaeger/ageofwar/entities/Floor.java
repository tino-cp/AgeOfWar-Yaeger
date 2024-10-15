package com.github.hanyaeger.ageofwar.entities;

import com.github.hanyaeger.ageofwar.AgeOfWar;
import com.github.hanyaeger.ageofwar.entities.projectiles.Projectile;
import com.github.hanyaeger.ageofwar.entities.scenes.MainScene;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;

import java.util.List;

public class Floor extends RectangleEntity implements Collided {
    private static final double FLOOR_HEIGHT = 50;
    private static final double FLOOR_Y = AgeOfWar.getGameHeight() - FLOOR_HEIGHT;

    public Floor(Coordinate2D location, MainScene mainScene) {
        super(location, new Size(mainScene.getWidth(), FLOOR_HEIGHT));
        setVisible(false);
    }

    public static double getFloorY() {
        return FLOOR_Y;
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Projectile projectile) {
                projectile.remove();
            } else if (collider instanceof Rock rock) {
                rock.remove();
            }
        }
    }
}
