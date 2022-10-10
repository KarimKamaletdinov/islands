package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;

public interface Presenter {
    Unit[] getProtectors();
    Unit[] getAttackers();
    Bullet[] getProtectorsBullets();
    Bullet[] getAttackersBullets();
}
