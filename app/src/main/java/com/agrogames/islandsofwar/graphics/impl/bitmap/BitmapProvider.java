package com.agrogames.islandsofwar.graphics.impl.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class BitmapProvider {
    private final Context context;
    private Dictionary<String, Bitmap> cachedBitmaps = new Hashtable<>();

    public BitmapProvider(Context context) {
        this.context = context;
    }

    public Bitmap Load(String name){
        try {
            Bitmap cached = cachedBitmaps.get(name);
            if(cached != null){
                return cached;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open("textures/" + name));
            cachedBitmaps.put(name, bitmap);
            return bitmap;
        } catch (IOException e) {
            return Bitmap.createBitmap(new int[]{Color.RED}, 1, 1, Bitmap.Config.RGB_565);
        }
    }
}
