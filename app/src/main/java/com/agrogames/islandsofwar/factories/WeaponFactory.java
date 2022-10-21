package com.agrogames.islandsofwar.factories;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.types.UnitType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.types.WeaponType;

public class WeaponFactory {
    public static Weapon[] create(UnitType unitType){
        switch (unitType){
            case Tank:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(0, 0), 2.0f, WeaponType.TankTower, 6, 0.8f, 2, 2, 1)
                };
            case RocketLauncher:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(0, 0), 4.0f, WeaponType.RocketLauncherTower, 8, 0.2f, 10, 2.5f, 2, 1)
                };
            case TransportShip:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(2, 0), 5.0f, WeaponType.TransportShipTower, 4, 0.1f, 10, 2, 1),
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(1, 0), 5.0f, WeaponType.TransportShipTower, 4, 0.1f, 10, 2, 1),
                };
            default:
                return new Weapon[0];
        }
    }
}
