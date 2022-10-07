package com.agrogames.islandsofwar.engine.impl.game;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameObjectProvider implements com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider {
    private final GameObject[] our;
    private final GameObject[] enemies;
    private final GameObject[] all;

    public GameObjectProvider(GameObject[] our, GameObject[] enemies) {
        this.our = our;
        this.enemies = enemies;
        List<GameObject> all = new ArrayList<>(Arrays.asList(our));
        all.addAll(Arrays.asList(enemies));
        this.all = all.toArray(new GameObject[0]);
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
    public GameObject[] getAll() {
        return all;
    }
}
