package com.agrogames.islandsofwar.engine.abs.movable;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

public interface MovableObject extends IUnit {
    void setGoal(Cell goal);
}
