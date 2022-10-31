package com.agrogames.islandsofwar.types;

import android.util.Log;

import com.agrogames.islandsofwar.render.impl.ObjectState;

public class GameObjectTypeMapper {
    public static TextureBitmap convert(RenderableObjectType type, ObjectState state){
        switch (type){
            case Tank:
                if (state == ObjectState.Selected) return TextureBitmap.TankSelected;
                else if (state == ObjectState.Destroyed) return TextureBitmap.TankDestroyed;
                return TextureBitmap.Tank;
            case TankTower:
                return TextureBitmap.TankTower;
            case TankBullet:
                return TextureBitmap.TankBullet;
            case RocketLauncher:
                if (state == ObjectState.Selected) return TextureBitmap.RocketLauncherSelected;
                else if (state == ObjectState.Destroyed) return TextureBitmap.RocketLauncherDestroyed;
                return TextureBitmap.RocketLauncher;
            case RocketLauncherTower:
                return TextureBitmap.RocketLauncherTower;
            case Rocket:
                return TextureBitmap.Rocket;
            case LandingCraft:
                if (state == ObjectState.Selected) return TextureBitmap.LandingCraftSelected;
                else if (state == ObjectState.Destroyed) return TextureBitmap.LandingCraftDestroyed;
                return TextureBitmap.LandingCraft;
            case TransportShip:
                if (state == ObjectState.Selected) return TextureBitmap.TransportShipSelected;
                else if (state == ObjectState.Destroyed) return TextureBitmap.TransportShipDestroyed;
                return TextureBitmap.TransportShip;
            case TransportShipTower:
                return TextureBitmap.TransportShipTower;
            case TransportShipBullet:
                return TextureBitmap.TransportShipBullet;
            case Pit:
                return TextureBitmap.Pit;
            case Bang:
                return TextureBitmap.Bang;
            case BigPit:
                return TextureBitmap.BigPit;
            case BigBang:
                return TextureBitmap.BigBang;
            case Bomber:
                if(state == ObjectState.Picture) return TextureBitmap.BomberPicture;
                else if(state == ObjectState.Selected) return TextureBitmap.BomberPictureSelected;
                else if (state == ObjectState.Destroyed) return TextureBitmap.BomberDestroyed;
                return TextureBitmap.Bomber;
            case Bomb:
                return TextureBitmap.Bomb;
            case AirDefence:
                return TextureBitmap.AirDefence;
            case AirDefenceTower:
                return TextureBitmap.AirDefenceTower;
            case None:
            default:
                Log.e("IOW", "Cannot specify TextureBitmap for RenderableObjectType " + type);
                return TextureBitmap.Error;
        }
    }
}
