package com.agrogames.islandsofwar.engine.abs.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

import java.util.UUID;

public interface Unit extends MapObject, RenderableObject, UpdatableObject {
    UnitType getType();
    IntValue getHealth();
    Point getLocation();
    Weapon[] getWeapons();
    void loseHealth(int lost);
}