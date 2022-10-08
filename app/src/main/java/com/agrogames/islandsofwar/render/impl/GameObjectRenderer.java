package com.agrogames.islandsofwar.render.impl;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class GameObjectRenderer {
    public static void render(GameObject object, TextureDrawer drawer){
        TextureProvider.getTexture(object).render(drawer);
    }
}
