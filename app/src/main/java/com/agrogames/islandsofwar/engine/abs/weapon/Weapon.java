package com.agrogames.islandsofwar.engine.abs.weapon;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;
import com.agrogames.islandsofwar.types.WeaponType;

public interface Weapon extends RenderableObject, UpdatableObject {
    void setOwner(Unit owner);
    Unit getOwner();
    Point getLocation();
    WeaponType getType();
    float getLongRange();
    int getDamage();
    float getSpeed();
    int getFlightHeight();
    int getTargetHeight();
}
