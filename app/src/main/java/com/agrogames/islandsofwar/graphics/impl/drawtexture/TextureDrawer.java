package com.agrogames.islandsofwar.graphics.impl.drawtexture;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.impl.gl.Texture;

import java.util.ArrayList;
import java.util.List;

public class TextureDrawer implements com.agrogames.islandsofwar.graphics.abs.TextureDrawer {
    private final List<Texture> textures = new ArrayList<>();
    private final BitmapProvider bitmapProvider;

    public TextureDrawer(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public void DrawTexture(float x, float y, TextureBitmap bitmap, float width, float height, float rotation) {
        textures.add(new Texture(x, y, bitmapProvider.Load(bitmap.name), width, height, rotation));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void DrawTexture(float x, float y, TextureBitmap bitmap, float rotation) {
        Bitmap b = bitmapProvider.Load(bitmap.name);
        Color color = b.getColor(0, 0);
        float alpha = color.alpha();
        textures.add(new Texture(x, y, b, b.getWidth() / 50f, b.getHeight() / 50f, rotation));
    }

    public Texture[] GetTextures(){
        Texture[] result = textures.toArray(new Texture[0]);
        textures.clear();
        return result;
    }
}
