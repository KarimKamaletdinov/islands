package com.agrogames.islandsofwar.render.impl;

import android.util.Pair;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class ObjectRenderer {
    private final TextureDrawer drawer;

    public ObjectRenderer(TextureDrawer drawer) {
        this.drawer = drawer;
    }

    public float render(RenderableObject object){
        return simpleRender(object, TextureMapper.join(object.getTexture()));
    }

    public float render(RenderableObject object, String state){
        return simpleRender(object, TextureMapper.join(object.getTexture(), state));
    }

    private float simpleRender(RenderableObject object, String texture){
        Point location = object.getLocation();
        Pair<Float, Float> size = drawer.drawTexture(location.x, location.y, texture, object.getRotation());
        return size.first > size.second ? size.first : size.second;
    }
}
