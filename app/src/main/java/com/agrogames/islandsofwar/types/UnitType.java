package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum UnitType implements RenderableObjectTypeConvertable {
    Tank, TransportShip, LandingCraft, RocketLauncher;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case Tank:
                return RenderableObjectType.Tank;
            case TransportShip:
                return RenderableObjectType.TransportShip;
            case LandingCraft:
                return RenderableObjectType.LandingCraft;
            case RocketLauncher:
                return RenderableObjectType.RocketLauncher;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for UnitType " + this);
                return RenderableObjectType.None;
        }
    }
}
