package com.agrogames.islandsofwar.engine.abs.unit;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum UnitType implements RenderableObjectTypeConvertable {
    Tank, TransportShip, LandingCraft;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case Tank:
                return RenderableObjectType.Tank;
            case TransportShip:
                return RenderableObjectType.TransportShip;
            case LandingCraft:
                return RenderableObjectType.LandingCraft;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for UnitType " + this);
                return RenderableObjectType.None;
        }
    }
}
