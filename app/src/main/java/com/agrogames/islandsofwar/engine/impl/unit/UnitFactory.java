package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;

import java.util.UUID;

public class UnitFactory {
    public static GameObject Car(int x, int y){
        return new LandUnit(UUID.randomUUID(), new Point(x, y), 10, 10);
    }
}
