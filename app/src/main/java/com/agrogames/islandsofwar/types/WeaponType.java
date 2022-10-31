package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.types.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum WeaponType implements RenderableObjectTypeConvertable {
    TankTower,
    TransportShipTower,
    AirDefenceTower,
    RocketLauncherTower, ShipDefenceTower;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case TankTower:
                return RenderableObjectType.TankTower;
            case TransportShipTower:
                return RenderableObjectType.TransportShipTower;
            case RocketLauncherTower:
                return RenderableObjectType.RocketLauncherTower;
            case AirDefenceTower:
                return RenderableObjectType.AirDefenceTower;
            case ShipDefenceTower:
                return RenderableObjectType.ShipDefenceTower;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for WeaponType " + this);
                return RenderableObjectType.None;
        }
    }
}
