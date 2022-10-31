package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectTypeConvertable;

public enum AnotherObjectType implements RenderableObjectTypeConvertable {
    Pit, Bang, BigBang, BigPit;

    @Override
    public RenderableObjectType toRenderableObjectType() {
        switch (this) {
            case Pit:
                return RenderableObjectType.Pit;
            case Bang:
                return RenderableObjectType.Bang;
            case BigPit:
                return RenderableObjectType.BigPit;
            case BigBang:
                return RenderableObjectType.BigBang;
            default:
                Log.e("IOW", "Cannot provide RenderableObjectType for AnotherObjectType: " + this);
                return RenderableObjectType.None;
        }
    }
}
