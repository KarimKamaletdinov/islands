package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;

import java.util.ArrayList;
import java.util.List;

public class TextureProvider {
    public static RenderableTexture[] getTextures(GameObject object){
        List<RenderableTexture> result = new ArrayList<>();
        int i = 0;
        for (Cell cell: object.GetTerritory()){
            result.add(new RenderableTexture(cell.x, cell.y, 1, 1, 0,
                    GameObjectTypeMapper.convert(object.getType(), i)));
            i++;
        }
        return result.toArray(new RenderableTexture[0]);
    }
}
