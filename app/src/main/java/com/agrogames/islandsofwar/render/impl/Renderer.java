package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.render.abs.Presenter;

public class Renderer implements com.agrogames.islandsofwar.render.abs.Renderer {
    private final Presenter presenter;

    public Renderer(Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void render(TextureDrawer drawer) {
        drawer.DrawTexture(10, 5, TextureBitmap.Background, 20, 10, 0);
        for (GameObject object: presenter.getAttackers()){
            GameObjectRenderer.render(object, drawer);
        }
        for (GameObject object: presenter.getProtectors()){
            GameObjectRenderer.render(object, drawer);
        }
    }
}
