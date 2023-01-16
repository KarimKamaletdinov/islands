package com.agrogames.islandsofwar.engine.abs.unit;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

public interface IUnit extends MapObject, RenderableObject, UpdatableObject {
    IntValue getHealth();
    Point getLocation();
    int getMinDamage();
    IWeapon[] getWeapons();
    void loseHealth(int lost);
    void addTsd(float deltaTime);
}
