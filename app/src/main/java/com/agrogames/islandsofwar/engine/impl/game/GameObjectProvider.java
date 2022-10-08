package com.agrogames.islandsofwar.engine.impl.game;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameObjectProvider implements com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider {
    private final GameObject[] our;
    private final GameObject[] enemies;
    private final MapObject[] all;

    public GameObjectProvider(GameObject[] our, GameObject[] enemies, MapObject[] mapObjects) {
        this.our = our;
        this.enemies = enemies;
        List<MapObject> all = new ArrayList<>(Arrays.asList(our));
        all.addAll(Arrays.asList(enemies));
        all.addAll(Arrays.asList(mapObjects));
        this.all = all.toArray(new MapObject[0]);
    }

    @Override
    public GameObject[] getOur() {
        return our;
    }

    @Override
    public GameObject[] getEnemies() {
        return enemies;
    }

    @Override
    public MapObject[] getAll() {
        return all;
    }
}
