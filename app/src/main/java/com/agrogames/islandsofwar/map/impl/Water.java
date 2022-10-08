package com.agrogames.islandsofwar.map.impl;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

public class Water implements MapObject {
    private final Cell location;

    public Water(Cell location) {
        this.location = location;
    }

    @Override
    public Cell[] GetTerritory() {
        return new Cell[]{location};
    }
}
