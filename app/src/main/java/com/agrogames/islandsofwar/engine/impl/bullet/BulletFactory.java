package com.agrogames.islandsofwar.engine.impl.bullet;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.bullet.BulletType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;

public class BulletFactory {
    public static Bullet Create(Weapon weapon){
        switch (weapon.getType()){
            case TankTower:
                return new Bullet(BulletType.TankBullet, weapon.getLocation(), 2, 2, weapon.getLongRange(), weapon.getOwner());
            default:
                Log.e("IOW", "Bullet not specified for WeaponType " + weapon.getType());
                return new Bullet(BulletType.TankBullet, weapon.getLocation(), 2, 2, weapon.getLongRange(), weapon.getOwner());
        }
    }
}
