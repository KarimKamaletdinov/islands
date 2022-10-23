package com.agrogames.islandsofwar.graphics.abs;

import android.util.Pair;

import com.agrogames.islandsofwar.types.TextureBitmap;

public interface TextureDrawer {
    void translate(float x, float y);
    void drawTexture(float x, float y, TextureBitmap bitmap, float width, float height, float rotation);
    Pair<Float, Float> drawTexture(float x, float y, TextureBitmap bitmap, float rotation);
}

