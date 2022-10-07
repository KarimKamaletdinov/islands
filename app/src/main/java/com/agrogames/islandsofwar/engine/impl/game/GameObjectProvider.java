package com.agrogames.islandsofwar.engine.impl.game;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;

public class GameObjectProvider implements com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider {
    private final GameObject[] our;
    private final GameObject[] enemies;

    public GameObjectProvider(GameObject[] our, GameObject[] enemies) {
        this.our = our;
        this.enemies = enemies;
    }

    @Override
    public GameObject[] getOur() {
        return our;
    }

    @Override
    public GameObject[] getEnemies() {
        return enemies;
    }
}
