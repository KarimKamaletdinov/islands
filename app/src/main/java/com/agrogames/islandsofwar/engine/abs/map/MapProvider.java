package com.agrogames.islandsofwar.engine.abs.map;

import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface MapProvider {
    Unit[] getOur();
    Unit[] getEnemies();
    MapObject[] getAll();
}
