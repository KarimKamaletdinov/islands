package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

public interface Presenter {
    IUnit[] getProtectors();
    IUnit[] getAttackers();
    RenderableObject[] getOther();
    IBullet[] getProtectorsBullets();
    IBullet[] getAttackersBullets();
    GameState getState();
    void addPlane(IUnit plane);
}
