package com.agrogames.islandsofwar.graphics.impl.drawtexture;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.impl.gl.Texture;

import java.util.ArrayList;
import java.util.List;

public class TextureDrawer implements com.agrogames.islandsofwar.graphics.abs.TextureDrawer {
    private final List<Texture> textures = new ArrayList<>();
    private final BitmapProvider bitmapProvider;
    private int currentX;
    private int currentY;

    public TextureDrawer(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public void translate(float x, float y) {
        currentX += x;
        currentY += y;
    }

    @Override
    public void drawTexture(float x, float y, String bitmap, float width, float height, float rotation) {
        textures.add(new Texture(x + currentX, y + currentY, bitmapProvider.load(bitmap), width, height, rotation));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Pair<Float, Float> drawTexture(float x, float y, String bitmap, float rotation) {
        Bitmap b = bitmapProvider.load(bitmap);
        float width = b.getWidth() / 50f;
        float height = b.getHeight() / 50f;
        textures.add(new Texture(x + currentX, y + currentY, b, width, height, rotation));
        return new Pair<>(width, height);
    }

    @Override
    public Pair<Float, Float> getSize(String bitmap) {
        Bitmap b = bitmapProvider.load(bitmap);
        float width = b.getWidth() / 50f;
        float height = b.getHeight() / 50f;
        return new Pair<>(width, height);
    }

    public Texture[] GetTextures(){
        Texture[] result = textures.toArray(new Texture[0]);
        textures.clear();
        return result;
    }
}
