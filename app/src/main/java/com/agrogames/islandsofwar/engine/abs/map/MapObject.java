package com.agrogames.islandsofwar.engine.abs.map;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

public interface MapObject {
    Cell[] GetTerritory();
    boolean isMoving();
    int getHeight();
}
