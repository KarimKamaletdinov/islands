package com.agrogames.islandsofwar.engine.abs.map;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

public interface MapObject {
    Cell[] getTerritory();
    boolean isMoving();
    int getHeight();
}
