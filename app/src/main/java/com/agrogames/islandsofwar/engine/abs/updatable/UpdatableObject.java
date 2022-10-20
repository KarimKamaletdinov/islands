package com.agrogames.islandsofwar.engine.abs.updatable;

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;

public interface UpdatableObject {
    void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, float deltaTime);
}
