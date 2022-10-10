package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class GameObjectRenderer {
    public static void render(RenderableObject object, TextureDrawer drawer){
        TextureProvider.getTexture(object).render(drawer);
    }
}
