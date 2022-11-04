package com.agrogames.islandsofwar.graphics.impl.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class BitmapProvider {
    private final Dictionary<String, BitmapDescriptor> cachedBitmaps = new Hashtable<>();

    public BitmapProvider(Context context, int mProgram) {
        try {
            String[] textures = loadFolder(context, "textures").toArray(new String[0]);

            GLES20.glUseProgram(mProgram);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            int[] textureUnit = new int[textures.length];
            GLES20.glGenTextures(textureUnit.length, textureUnit, 0);
            for (int i = 0; i < textures.length; i++){
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[i]);
                Bitmap bitmap = loadFile(context, textures[i].substring(9));
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

                BitmapDescriptor descriptor = new BitmapDescriptor();
                descriptor.id = textureUnit[i];
                descriptor.width = bitmap.getWidth();
                descriptor.height = bitmap.getHeight();
                cachedBitmaps.put(textures[i].substring(9), descriptor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BitmapDescriptor load(String name){
        BitmapDescriptor b = cachedBitmaps.get(name + ".png");
        return b;
    }

    private List<String> loadFolder(Context context, String name) throws IOException{
        List<String> result = new ArrayList<>();
        for (String asset: context.getAssets().list(name)) {
            if(asset.contains(".")){
                result.add(name + "/" + asset);
            } else {
                result.addAll(loadFolder(context, name + "/" + asset));
            }
        }
        return result;
    }

    private Bitmap loadFile(Context context, String name) throws IOException{
        return BitmapFactory.decodeStream(context.getAssets().open("textures/" + name));
    }
}
