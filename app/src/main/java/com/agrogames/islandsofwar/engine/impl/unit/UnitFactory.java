package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.impl.weapon.WeaponFactory;

public class UnitFactory {
    public static Unit Tank(int x, int y){
        return new LandUnit(UnitType.Tank, new Point(x, y), WeaponFactory.create(UnitType.Tank), 10, 2f);
    }
}
