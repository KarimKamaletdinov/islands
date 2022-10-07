package com.agrogames.islandsofwar.engine.impl.map;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.MovableObject;
import com.agrogames.islandsofwar.engine.abs.map.FutureMap;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.ArrayList;
import java.util.List;

public class FutureMapFactory {
    public static FutureMap Create(int width, int height, MapObject[] objects){
        List<boolean[][]> map = new ArrayList<>();
        map.add(new boolean[width][height]);

        for (MapObject object : objects) {
            addTerritory(map, 0, object.GetTerritory());

            if(object instanceof MovableObject){
                MovableObject movableObject = (MovableObject) object;
                int tick = 1;
                while (true){
                    Cell[] territory = movableObject.GetTerritory(tick);
                    if (territory.length == 0){
                        break;
                    }
                    while (map.size() < tick){
                        map.add(new boolean[width][height]);
                    }
                    addTerritory(map, tick, territory);
                    tick++;
                }
            }
        }
        return new com.agrogames.islandsofwar.engine.impl.map.FutureMap(map.toArray(new boolean[0][0][0]));
    }

    private static void addTerritory(List<boolean[][]> map, int tick, Cell[] territory) {
        for (Cell cell: territory) {
            map.get(tick)[cell.x][cell.y] = true;
        }
    }
}
