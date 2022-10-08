package com.agrogames.islandsofwar.engine.abs;

import com.agrogames.islandsofwar.engine.abs.game.Bullet;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;

public interface Engine {
    void update(float deltaTime);
    GameObject[] getProtectors();
    GameObject[] getAttackers();
    Bullet[] getProtectorsBullets();
    Bullet[] getAttackersBullets();
}
