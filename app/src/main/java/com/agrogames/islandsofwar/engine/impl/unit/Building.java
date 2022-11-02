package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;

public class Building extends LandUnit{
    public Building(String texture, Cell location, IWeapon[] weapons, int health) {
        super(texture, location, weapons, health, 0, 0);
    }

    @Override
    protected void rotate(float deltaTime) {}

    @Override
    protected void move(MapObject[] all, float deltaTime) {}

    @Override
    protected void think(MapProvider provider) {}
}
