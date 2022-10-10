package com.agrogames.islandsofwar.engine.abs.movable;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface MovableObject extends Unit {
    void setGoal(Cell goal);
}
