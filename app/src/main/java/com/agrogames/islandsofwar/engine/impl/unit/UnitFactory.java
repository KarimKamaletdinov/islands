package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.impl.weapon.WeaponFactory;

public class UnitFactory {
    public static Unit Tank(int x, int y){
        return new LandUnit(UnitType.Tank, new Cell(x, y), WeaponFactory.create(UnitType.Tank), 10, 1, 0.5f);
    }

    public static Unit TransportShip(int x, int y, TransportUnit[] units){
        return new BigShip(UnitType.TransportShip, new Cell(x, y), WeaponFactory.create(UnitType.TransportShip), 10, 1f, 0.1f, units, 5);
    }

    public static Unit LandingCraft(int x, int y){
        return new SmallShip(UnitType.LandingCraft, new Cell(x, y), WeaponFactory.create(UnitType.LandingCraft), 10, 1f, 0.1f);
    }
}
