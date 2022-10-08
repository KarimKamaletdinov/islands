package com.agrogames.islandsofwar.engine.impl.unit;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;

class BulletFactory {
    public static Bullet Create(GameObject unit){
        switch (unit.getType()){
            case Tank:
                return new Bullet(GameObjectType.TankBullet, unit.getLocation(), 2, 2, 10);
            default:
                Log.e("IOW", "Bullet not specified for GameObjectType " + unit.getType());
                return null;
        }
    }
}
