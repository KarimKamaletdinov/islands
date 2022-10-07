package com.agrogames.islandsofwar.engine.abs.game;

public interface GameObjectProvider {
    GameObject[] getOur();
    GameObject[] getEnemies();
}
