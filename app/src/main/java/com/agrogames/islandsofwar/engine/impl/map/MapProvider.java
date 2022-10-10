package com.agrogames.islandsofwar.engine.impl.map;

import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapProvider implements com.agrogames.islandsofwar.engine.abs.map.MapProvider {
    private final Unit[] our;
    private final Unit[] enemies;
    private final MapObject[] all;

    public MapProvider(Unit[] our, Unit[] enemies, MapObject[] mapObjects) {
        this.our = our;
        this.enemies = enemies;
        List<MapObject> all = new ArrayList<>(Arrays.asList(our));
        all.addAll(Arrays.asList(enemies));
        all.addAll(Arrays.asList(mapObjects));
        this.all = all.toArray(new MapObject[0]);
    }

    @Override
    public Unit[] getOur() {
        return our;
    }

    @Override
    public Unit[] getEnemies() {
        return enemies;
    }

    @Override
    public MapObject[] getAll() {
        return all;
    }
}
