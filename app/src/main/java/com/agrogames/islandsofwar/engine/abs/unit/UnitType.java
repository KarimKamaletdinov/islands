package com.agrogames.islandsofwar.engine.abs.unit;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum UnitType implements RenderableObjectTypeConvertable {
    Tank;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this){
            case Tank:
                return RenderableObjectType.Tank;
            default:
                Log.e("IOW", "Cannot specify RenderableObjectType for UnitType " + this);
                return RenderableObjectType.None;
        }
    }
}
