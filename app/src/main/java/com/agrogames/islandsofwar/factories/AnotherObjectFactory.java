package com.agrogames.islandsofwar.factories;

import com.agrogames.islandsofwar.engine.abs.another.AnotherObject;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.types.AnotherObjectType;
import com.agrogames.islandsofwar.types.BulletType;

public class AnotherObjectFactory {
    public static AnotherObject bang(RenderableObject unit){
        return new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                unit.getLocation(), unit.getRotation(), AnotherObjectType.Bang,
                new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                        unit.getLocation(), unit.getRotation(), AnotherObjectType.Pit, 120
                ), 3);
    }
    public static AnotherObject bigBang(RenderableObject unit){
        return new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                unit.getLocation(), unit.getRotation(), AnotherObjectType.BigBang,
                new com.agrogames.islandsofwar.engine.impl.another.AnotherObject(
                        unit.getLocation(), unit.getRotation(), AnotherObjectType.BigPit, 120
                ), 3);
    }
}
