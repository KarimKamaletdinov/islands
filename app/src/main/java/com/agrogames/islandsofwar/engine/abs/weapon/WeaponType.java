package com.agrogames.islandsofwar.engine.abs.weapon;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum WeaponType implements RenderableObjectTypeConvertable {
    TankTower;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case TankTower:
                return RenderableObjectType.TankTower;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for WeaponType " + this);
                return RenderableObjectType.None;
        }
    }
}