package com.agrogames.islandsofwar.graphics.impl.drawtexture;

import android.graphics.Bitmap;
import android.util.Pair;
import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.impl.gl.Texture;

public class TextureDrawer implements com.agrogames.islandsofwar.graphics.abs.TextureDrawer {
    private final BitmapProvider bitmapProvider;
    private int currentX;
    private int currentY;
    public int mProgram;
    public float[] vPMatrix;

    public TextureDrawer(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public void translate(float x, float y) {
        currentX += x;
        currentY += y;
    }

    @Override
    public void drawTexture(float x, float y, String texture, float width, float height, float rotation) {
        new Texture(x + currentX, y + currentY, bitmapProvider.load(texture), width, height, rotation).render(mProgram, vPMatrix);
    }

    @Override
    public Pair<Float, Float> drawTexture(float x, float y, String texture, float rotation) {
        Bitmap b = bitmapProvider.load(texture);
        float width = b.getWidth() / 50f;
        float height = b.getHeight() / 50f;
        new Texture(x + currentX, y + currentY, b, width, height, rotation).render(mProgram, vPMatrix);
        return new Pair<>(width, height);
    }
}
