package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.UUID;

public interface GameObject extends MapObject {
    UUID getId();
    GameObjectType getType();
    IntValue getHealth();
    Point getLocation();
    float getRotation();
    void loseHealth(int lost);
    void update(GameObjectProvider provider, BulletAdder adder, float deltaTime);
}
