package com.agrogames.islandsofwar.engine.abs.updatable;

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;

public interface UpdatableObject {
    void update(MapProvider provider, BulletAdder adder, float deltaTime);
}
