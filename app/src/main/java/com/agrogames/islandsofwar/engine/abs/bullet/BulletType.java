package com.agrogames.islandsofwar.engine.abs.bullet;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum BulletType implements RenderableObjectTypeConvertable {
    TankBullet,
    TransportShipBullet;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case TankBullet:
                return RenderableObjectType.TankBullet;
            case TransportShipBullet:
                return RenderableObjectType.TransportShipBullet;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for BulletType " + this);
                return RenderableObjectType.None;
        }
    }
}
