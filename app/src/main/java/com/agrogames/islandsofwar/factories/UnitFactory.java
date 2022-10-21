package com.agrogames.islandsofwar.factories;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.impl.unit.BigShip;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.SmallShip;
import com.agrogames.islandsofwar.types.UnitType;
import com.agrogames.islandsofwar.factories.WeaponFactory;

public class UnitFactory {
    public static Unit Tank(int x, int y){
        return new LandUnit(UnitType.Tank, new Cell(x, y), WeaponFactory.create(UnitType.Tank), 10, 1, 0.5f);
    }

    public static Unit TransportShip(int x, int y, TransportUnit[] units){
        return new BigShip(UnitType.TransportShip, new Cell(x, y), WeaponFactory.create(UnitType.TransportShip), 100, 1f, 0.1f, units, 5);
    }

    public static Unit LandingCraft(int x, int y){
        return new SmallShip(UnitType.LandingCraft, new Cell(x, y), WeaponFactory.create(UnitType.LandingCraft), 10, 1f, 0.1f);
    }

    public static Unit RocketLauncher(int x, int y){
        return new LandUnit(UnitType.RocketLauncher, new Cell(x, y), WeaponFactory.create(UnitType.RocketLauncher), 5, 0.8f, 0.3f);
    }
}
