package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

public interface MovableObject extends GameObject{
    Cell[] GetTerritory(int tick);
}
