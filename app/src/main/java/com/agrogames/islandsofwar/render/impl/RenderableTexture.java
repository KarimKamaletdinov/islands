package com.agrogames.islandsofwar.render.impl;

import android.util.Pair;

import com.agrogames.islandsofwar.types.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class RenderableTexture {
    private final float x;
    private final float y;
    public Float width;
    public Float height;
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
            drawer.drawTexture(x, y, bitmap, width, height, rotation);
        } else {
            Pair<Float, Float> p = drawer.drawTexture(x, y, bitmap, rotation);
            width = p.first;
            height = p.second;
        }
    }
}
