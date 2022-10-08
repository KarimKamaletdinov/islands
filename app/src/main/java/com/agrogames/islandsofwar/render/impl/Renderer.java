package com.agrogames.islandsofwar.render.impl;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.game.Bullet;
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
        drawer.DrawTexture(7.5f, 5, TextureBitmap.Background, 15, 10, 0);
        for (GameObject object: presenter.getAttackers()){
            GameObjectRenderer.render(object, drawer);
        }
        for (GameObject object: presenter.getProtectors()){
            GameObjectRenderer.render(object, drawer);
        }
        for (Bullet object: presenter.getAttackersBullets()){
            GameObjectRenderer.render(object, drawer);
        }
        for (Bullet object: presenter.getProtectorsBullets()){
            GameObjectRenderer.render(object, drawer);
        }
    }
}
