package com.agrogames.islandsofwar.graphics.impl.drawtexture;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapDescriptor;
import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.impl.gl.Texture;

import java.util.ArrayList;
import java.util.List;

public class TextureDrawer implements com.agrogames.islandsofwar.graphics.abs.TextureDrawer {
    private final List<Texture> textures = new ArrayList<>();
    private final BitmapProvider bitmapProvider;
    private float currentX;
    private float currentY;
    private float currentZoom = 1;

    public TextureDrawer(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public void drawTexture(float x, float y, String bitmap, float width, float height, float rotation) {
        textures.add(new Texture(x * currentZoom + currentX, y * currentZoom + currentY, bitmapProvider.load(bitmap).id, width * currentZoom, height * currentZoom, rotation));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Pair<Float, Float> drawTexture(float x, float y, String bitmap, float rotation) {
        BitmapDescriptor b = bitmapProvider.load(bitmap);
        float width = b.width / 50f;
        float height = b.height / 50f;
        textures.add(new Texture(x * currentZoom + currentX, y * currentZoom + currentY, b.id, width * currentZoom, height * currentZoom, rotation));
        return new Pair<>(width, height);
    }

    @Override
    public Pair<Float, Float> getSize(String bitmap) {
        BitmapDescriptor b = bitmapProvider.load(bitmap);
        float width = b.width / 50f;
        float height = b.height / 50f;
        return new Pair<>(width, height);
    }

    @Override
    public void translate(float x, float y) {
        currentX += x;
        currentY += y;
    }

    @Override
    public void scale(float zoom) {
        currentZoom *= zoom;
    }

    public Texture[] GetTextures(){
        Texture[] result = textures.toArray(new Texture[0]);
        textures.clear();
        return result;
    }
}
