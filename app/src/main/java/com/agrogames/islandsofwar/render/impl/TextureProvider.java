package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;

public class TextureProvider {
    public static RenderableTexture getTexture(RenderableObject object){
        Point location = object.getLocation();
        return new RenderableTexture(location.x, location.y, object.getRotation(), GameObjectTypeMapper.convert(object.getType().toRenderableObjectType()));
    }
}
