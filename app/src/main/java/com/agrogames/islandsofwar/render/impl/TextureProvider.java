package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;

import java.util.ArrayList;
import java.util.List;

public class TextureProvider {
    public static RenderableTexture getTexture(GameObject object){
        Point location = object.getLocation();
        return new RenderableTexture(location.x, location.y, object.getRotation(),
                GameObjectTypeMapper.convert(object.getType()));
    }
}
