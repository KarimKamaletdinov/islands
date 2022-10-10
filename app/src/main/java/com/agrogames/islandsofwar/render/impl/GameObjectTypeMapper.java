package com.agrogames.islandsofwar.render.impl;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;

public class GameObjectTypeMapper {
    public static TextureBitmap convert(RenderableObjectType type){
        switch (type){
            case Tank:
                return TextureBitmap.Tank;
            case TankTower:
                return TextureBitmap.TankTower;
            case TankBullet:
                return TextureBitmap.TankBullet;
            default:
                Log.e("IOW", "Cannot specify TextureBitmap for RenderableObjectType " + type);
                return TextureBitmap.Error;
        }
    }
}
