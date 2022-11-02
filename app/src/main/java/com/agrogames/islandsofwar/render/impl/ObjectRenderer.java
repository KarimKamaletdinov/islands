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
        return render(object, TextureMapper.join(object.getTexture()));
    }

    public float render(RenderableObject object, String state){
        Point location = object.getLocation();
        Pair<Float, Float> size = drawer.drawTexture(location.x, location.y, TextureMapper.join(object.getTexture(), state), object.getRotation());
        return size.first > size.second ? size.first : size.second;
    }
}
