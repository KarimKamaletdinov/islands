package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.map.MapObject;

public interface GameObjectProvider {
    GameObject[] getOur();
    GameObject[] getEnemies();
    MapObject[] getAll();
}
