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
                                new Point(0, 0), 2.0f, WeaponType.TankTower, 6, 0.8f, 2, 5, 1)
                };
            case RocketLauncher:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(0, 0), 4.0f, WeaponType.RocketLauncherTower, 11, 0.2f, 10, 2, 3, 1, true)
                };
            case TransportShip:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(2, 0), 5.0f, WeaponType.TransportShipTower, 10, 0.1f, 5,
                                7, 2, 1, new Point[] {
                                        new Point(0, -0.05f), new Point(0, 0.15f)
                                }),
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(1, 0), 5.0f, WeaponType.TransportShipTower, 10, 0.1f, 5,
                                7, 2, 1, new Point[] {
                                        new Point(0, -0.05f), new Point(0, 0.15f)
                                }),
                };
            case AirDefence:
                return new Weapon[]{
                        new com.agrogames.islandsofwar.engine.impl.weapon.Weapon(
                                new Point(0, 0), 4.0f, WeaponType.AirDefenceTower, 11, 0.5f, 10, 2, 4, 4, true)
                };
            default:
                return new Weapon[0];
        }
    }
}
