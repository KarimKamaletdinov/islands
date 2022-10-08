package com.agrogames.islandsofwar.render.impl;

import android.util.Log;

import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class RenderableTexture {
    private final float x;
    private final float y;
    private final Float width;
    private final Float height;
    private final float rotation;
    private final TextureBitmap bitmap;

    public RenderableTexture(float x, float y, float width, float height, float rotation, TextureBitmap bitmap) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.bitmap = bitmap;
    }

    public RenderableTexture(float x, float y, float rotation, TextureBitmap bitmap) {
        this.x = x;
        this.y = y;
        this.width = null;
        this.height = null;
        this.rotation = rotation;
        this.bitmap = bitmap;
    }

    public void render(TextureDrawer drawer){
        if(width != null && height != null){
            drawer.DrawTexture(x, y, bitmap, width, height, rotation);
        } else {
            drawer.DrawTexture(x, y, bitmap, rotation);
        }
    }
}
