package com.agrogames.islandsofwar.engine.abs;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface Engine {
    void update(float deltaTime);
    Unit[] getProtectors();
    Unit[] getAttackers();
    Bullet[] getProtectorsBullets();
    Bullet[] getAttackersBullets();
}
