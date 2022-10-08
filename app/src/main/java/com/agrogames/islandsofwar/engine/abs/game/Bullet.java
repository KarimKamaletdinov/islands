package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.common.Point;

public interface Bullet extends GameObject {
    void setGoal(Point goal);
}
