package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public class RenderableTexture {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
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

    public void render(TextureDrawer drawer){
        drawer.DrawTexture(x, y, bitmap, width, height, rotation);
    }
}
