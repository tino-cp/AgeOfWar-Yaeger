package com.github.hanyaeger.tutorial;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;

import javafx.scene.paint.Color;

import java.util.List;

public class FloorEntity extends RectangleEntity implements Collided {

    public FloorEntity(Coordinate2D location, MainScene mainScene) {
        super(location);
        setWidth(mainScene.getWidth());
        setHeight(50);
        setFill(Color.GRAY);
    }

    @Override
    public void onCollision(List<Collider> list) {
        for (Collider collider : list) {
            if (collider instanceof Projectile projectile) {
                projectile.remove();
            }
        }
    }
}
