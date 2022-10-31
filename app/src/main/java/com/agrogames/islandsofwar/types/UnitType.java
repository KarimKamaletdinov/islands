package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum UnitType implements RenderableObjectTypeConvertable {
    Tank, TransportShip, LandingCraft, RocketLauncher, AirDefence, Bomber;

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
            case Bomber:
                return RenderableObjectType.Bomber;
            case AirDefence:
                return RenderableObjectType.AirDefence;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for UnitType " + this);
                return RenderableObjectType.None;
        }
    }
}
