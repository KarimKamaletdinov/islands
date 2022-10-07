package com.agrogames.islandsofwar.graphics.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;

public class BitmapProvider {
    private final Context context;

    public BitmapProvider(Context context) {
        this.context = context;
    }

    public Bitmap Load(String name){
        try {
            return BitmapFactory.decodeStream(context.getAssets().open("textures/" + name));
        } catch (IOException e) {
            return Bitmap.createBitmap(new int[]{Color.RED}, 1, 1, Bitmap.Config.RGB_565);
        }
    }
}
