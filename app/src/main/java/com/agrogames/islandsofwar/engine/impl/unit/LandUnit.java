package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider;
import com.agrogames.islandsofwar.engine.abs.map.FutureMap;

import java.util.UUID;

class LandUnit extends Unit{
    public LandUnit(UUID id, Point location, int health) {
        super(id, location, health);
    }

    @Override
    public void update(FutureMap map, GameObjectProvider provider) {

    }
}
