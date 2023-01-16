package com.agrogames.islandsofwar.engine.abs.updatable;

import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;

public interface UpdatableObject {
    void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, GraphicsAdder graphicsAdder, float deltaTime);
}
