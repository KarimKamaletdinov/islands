package com.agrogames.islandsofwar.types;

import android.util.Log;

public class GameObjectTypeMapper {
    public static TextureBitmap convert(RenderableObjectType type, boolean selected){
        switch (type){
            case Tank:
                return selected ? TextureBitmap.TankSelected : TextureBitmap.Tank;
            case TankTower:
                return TextureBitmap.TankTower;
            case TankBullet:
                return TextureBitmap.TankBullet;
            case RocketLauncher:
                return selected ? TextureBitmap.RocketLauncherSelected : TextureBitmap.RocketLauncher;
            case RocketLauncherTower:
                return TextureBitmap.RocketLauncherTower;
            case Rocket:
                return TextureBitmap.Rocket;
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
