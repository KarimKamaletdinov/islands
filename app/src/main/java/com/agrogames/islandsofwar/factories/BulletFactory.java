package com.agrogames.islandsofwar.factories;

import android.util.Log;

import com.agrogames.islandsofwar.engine.impl.bullet.Bullet;
import com.agrogames.islandsofwar.types.BulletType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.types.WeaponType;

public class BulletFactory {
    public static Bullet create(Weapon weapon){
        return new Bullet(convert(weapon.getType()), weapon.getLocation(), weapon.getSpeed(), weapon.getDamage(),
                weapon.getLongRange(), weapon.getFlightHeight(), weapon.getTargetHeight(), weapon.getOwner());
    }

    private static BulletType convert(WeaponType weaponType){
        switch (weaponType){
            case TankTower:
                return BulletType.TankBullet;
            case RocketLauncherTower:
                return BulletType.Rocket;
            case TransportShipTower:
                return BulletType.TransportShipBullet;
            default:
                Log.e("IOW", "Bullet not specified for WeaponType " + weaponType);
                return BulletType.TankBullet;
        }
    }
}
