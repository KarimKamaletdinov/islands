package com.agrogames.islandsofwar.engine.abs;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;

public interface Engine {
    void update(int ticksPassed);
    GameObject[] getProtectors();
    GameObject[] getAttackers();
}
