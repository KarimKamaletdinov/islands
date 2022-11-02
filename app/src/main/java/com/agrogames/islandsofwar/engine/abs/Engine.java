package com.agrogames.islandsofwar.engine.abs;

import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

public interface Engine {
    void update(float deltaTime);
    IUnit[] getProtectors();
    IUnit[] getAttackers();
    RenderableObject[] getOther();
    IBullet[] getProtectorsBullets();
    IBullet[] getAttackersBullets();
    GameState getState();
    void addPlane(IUnit plane);
}
