package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;

public class GameObjectTypeMapper {
    public static TextureBitmap convert(GameObjectType type){
        switch (type){
            case Tank:
                return TextureBitmap.Tank;
            case TankBullet:
                return TextureBitmap.TankBullet;
            default:
                return TextureBitmap.Error;
        }
    }
}
