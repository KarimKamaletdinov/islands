package com.agrogames.islandsofwar.graphics.abs;

import android.util.Pair;

public interface TextureDrawer {
    void translate(float x, float y);
    void drawTexture(float x, float y, String bitmap, float width, float height, float rotation);
    Pair<Float, Float> drawTexture(float x, float y, String bitmap, float rotation);
    Pair<Float, Float> getSize(String bitmap);
}

