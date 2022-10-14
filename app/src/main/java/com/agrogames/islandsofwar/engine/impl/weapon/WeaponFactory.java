package com.agrogames.islandsofwar.engine.impl.weapon;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.abs.weapon.WeaponType;

public class WeaponFactory {
    public static Weapon[] create(UnitType unitType){
        switch (unitType){
            case Tank:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(0, 0), 2.0f, WeaponType.TankTower, 10, 0.8f)
                };
            default:
                return new Weapon[0];
        }
    }
}
