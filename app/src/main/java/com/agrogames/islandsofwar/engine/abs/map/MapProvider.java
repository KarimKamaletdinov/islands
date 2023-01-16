package com.agrogames.islandsofwar.engine.abs.map;

import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

public interface MapProvider {
    IUnit[] getOur();
    IUnit[] getEnemies();
    MapObject[] getAll();
}
