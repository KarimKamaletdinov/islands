package com.agrogames.islandsofwar.render.impl;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObjectType;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;

public class GameObjectTypeMapper {
    public static TextureBitmap convert(RenderableObjectType type, boolean selected){
        switch (type){
            case Tank:
                return selected ? TextureBitmap.TankSelected : TextureBitmap.Tank;
            case TankTower:
                return TextureBitmap.TankTower;
            case TankBullet:
                return TextureBitmap.TankBullet;
            case LandingCraft:
                return selected ? TextureBitmap.LandingCraftSelected : TextureBitmap.LandingCraft;
            case TransportShip:
                return selected ? TextureBitmap.TransportShipSelected : TextureBitmap.TransportShip;
            case TransportShipTower:
                return TextureBitmap.TransportShipTower;
            case TransportShipBullet:
                return TextureBitmap.TransportShipBullet;
            default:
                Log.e("IOW", "Cannot specify TextureBitmap for RenderableObjectType " + type);
                return TextureBitmap.Error;
        }
    }
}
