package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.types.UnitType;

public class Building extends LandUnit{
    public Building(UnitType type, Cell location, Weapon[] weapons, int health) {
        super(type, location, weapons, health, 0, 0);
    }

    @Override
    protected void rotate(float deltaTime) {}

    @Override
    protected void move(MapObject[] all, float deltaTime) {}

    @Override
    protected void think(MapProvider provider) {}
}
