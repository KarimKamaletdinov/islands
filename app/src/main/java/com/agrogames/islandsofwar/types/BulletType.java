package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum BulletType implements RenderableObjectTypeConvertable {
    TankBullet,
    TransportShipBullet,
    Bomb,
    Rocket;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case TankBullet:
                return RenderableObjectType.TankBullet;
            case TransportShipBullet:
                return RenderableObjectType.TransportShipBullet;
            case Rocket:
                return RenderableObjectType.Rocket;
            case Bomb:
                return RenderableObjectType.Bomb;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for BulletType " + this);
                return RenderableObjectType.None;
        }
    }
}

