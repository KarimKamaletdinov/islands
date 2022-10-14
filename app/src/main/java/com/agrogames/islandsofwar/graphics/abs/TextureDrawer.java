package com.agrogames.islandsofwar.graphics.abs;

import android.util.Pair;

public interface TextureDrawer {
    void DrawTexture(float x, float y, TextureBitmap bitmap, float width, float height, float rotation);
    Pair<Float, Float> DrawTexture(float x, float y, TextureBitmap bitmap, float rotation);
}

