package com.agrogames.islandsofwar.engine.abs.bullet;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

public interface Bullet extends RenderableObject, UpdatableObject {
    void setGoal(Point goal);
    BulletType getType();
    boolean hasStopped();
}