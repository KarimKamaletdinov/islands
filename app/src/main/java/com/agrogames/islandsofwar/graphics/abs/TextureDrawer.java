package com.agrogames.islandsofwar.graphics.abs;

import android.util.Pair;

import com.agrogames.islandsofwar.types.TextureBitmap;

public interface TextureDrawer {
    void DrawTexture(float x, float y, TextureBitmap bitmap, float width, float height, float rotation);
    Pair<Float, Float> DrawTexture(float x, float y, TextureBitmap bitmap, float rotation);
}

