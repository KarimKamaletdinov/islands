package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

public interface MovableObject extends GameObject{
    void setGoal(Cell goal);
}
