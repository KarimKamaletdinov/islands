package com.agrogames.islandsofwar.graphics.impl.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class BitmapProvider {
    private final Context context;
    private final Dictionary<String, Bitmap> cachedBitmaps = new Hashtable<>();

    public BitmapProvider(Context context) {
        this.context = context;
    }

    public Bitmap load(String name){
        Bitmap cached = cachedBitmaps.get(name);
        if(cached != null){
            return cached;
        }
        Bitmap bitmap = loadFromFile(name);
        cachedBitmaps.put(name, bitmap);
        return bitmap;
    }

    private Bitmap loadFromFile(String name){
        try {
            return BitmapFactory.decodeStream(context.getAssets().open("textures/" + name + ".png"));
        } catch (IOException e) {
            Log.e("IOW", "Cannot load bitmap with name " + name);
            return Bitmap.createBitmap(new int[]{Color.RED}, 1, 1, Bitmap.Config.RGB_565);
        }
    }
}
