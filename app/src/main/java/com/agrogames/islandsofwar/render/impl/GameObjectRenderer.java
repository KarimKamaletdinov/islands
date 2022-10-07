package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class GameObjectRenderer {
    public static void render(GameObject object, TextureDrawer drawer){
        for (RenderableTexture texture: TextureProvider.getTextures(object)){
            texture.render(drawer);
        }
    }
}
