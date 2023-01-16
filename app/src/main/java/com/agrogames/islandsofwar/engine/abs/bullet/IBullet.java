package com.agrogames.islandsofwar.engine.abs.bullet;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

public interface IBullet extends RenderableObject, UpdatableObject {
    void setGoal(Point goal);
    boolean hasStopped();

    String getCreationSound();
}
