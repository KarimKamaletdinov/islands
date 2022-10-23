package com.agrogames.islandsofwar.factories;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.impl.bullet.Bullet;
import com.agrogames.islandsofwar.types.BulletType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.types.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class BulletFactory {
    public static Bullet[] create(Weapon weapon){
        List<Bullet> result = new ArrayList<>();
        for(Point start : weapon.getBulletStarts()){
            result.add(new Bullet(convert(weapon.getType()), start, weapon.getSpeed(), weapon.getDamage(),
                    weapon.getLongRange(), weapon.getFlightHeight(), weapon.getTargetHeight(), weapon.getOwner()));
        }
        return result.toArray(new Bullet[0]);
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
