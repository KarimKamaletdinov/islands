package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;

import java.util.UUID;

public class UnitFactory {
    public static GameObject Tank(int x, int y){
        return new LandUnit(UUID.randomUUID(), GameObjectType.Tank, new Point(x, y), 10, 1f, 2f);
    }
}
