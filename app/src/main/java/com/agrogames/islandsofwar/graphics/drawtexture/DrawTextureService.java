package com.agrogames.islandsofwar.graphics.drawtexture;

import com.agrogames.islandsofwar.graphics.abstractions.TextureBitmap;
import com.agrogames.islandsofwar.graphics.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.gl.Texture;

import java.util.ArrayList;
import java.util.List;

public class DrawTextureService implements com.agrogames.islandsofwar.graphics.abstractions.DrawTextureService {
    private final List<Texture> textures = new ArrayList<>();
    private final BitmapProvider bitmapProvider;

    public DrawTextureService(BitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    @Override
    public void DrawTexture(float x, float y, TextureBitmap bitmap, float height, float width, float rotation) {
        textures.add(new Texture(x, y, bitmapProvider.Load(bitmap.name), width, height, rotation));
    }

    public Texture[] GetTextures(){
        Texture[] result = textures.toArray(new Texture[0]);
        textures.clear();
        return result;
    }
}
