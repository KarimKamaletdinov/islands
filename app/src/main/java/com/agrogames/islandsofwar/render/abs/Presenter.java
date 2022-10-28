package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface Presenter {
    Unit[] getProtectors();
    Unit[] getAttackers();
    RenderableObject[] getOther();
    Bullet[] getProtectorsBullets();
    Bullet[] getAttackersBullets();
    GameState getState();
}
