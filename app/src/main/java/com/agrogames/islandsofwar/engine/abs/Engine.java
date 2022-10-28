package com.agrogames.islandsofwar.engine.abs;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface Engine {
    void update(float deltaTime);
    Unit[] getProtectors();
    Unit[] getAttackers();
    RenderableObject[] getOther();
    Bullet[] getProtectorsBullets();
    Bullet[] getAttackersBullets();
    GameState getState();
}
