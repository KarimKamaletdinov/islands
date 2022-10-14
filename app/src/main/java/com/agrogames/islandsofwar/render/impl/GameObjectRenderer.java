package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class GameObjectRenderer {
    public static float render(RenderableObject object, TextureDrawer drawer, boolean selected){
        RenderableTexture texture = TextureProvider.getTexture(object, selected);
        texture.render(drawer);
        return texture.width > texture.height ? texture.width : texture.height;
    }


    public static float render(RenderableObject object, TextureDrawer drawer){
        return render(object, drawer, false);
    }
}
