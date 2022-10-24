package com.agrogames.islandsofwar.engine.abs.another;

import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.types.BulletType;

public class AnotherObjectFactory {
    public static AnotherObject bang(Unit unit){
        return new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                unit.getLocation(), unit.getRotation(), BulletType.AnotherObjectType.Bang,
                new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                        unit.getLocation(), unit.getRotation(), BulletType.AnotherObjectType.Pit, 120
                ), 3);
    }
}
