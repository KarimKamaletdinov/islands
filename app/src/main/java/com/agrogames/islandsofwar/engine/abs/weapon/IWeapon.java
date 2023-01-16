package com.agrogames.islandsofwar.engine.abs.weapon;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

public interface IWeapon extends RenderableObject, UpdatableObject {
    void setOwner(IUnit owner);
    Point getRelativeLocation();
    Point[] getBulletStarts();
    float getLongRange();
    int getDamage();
}
