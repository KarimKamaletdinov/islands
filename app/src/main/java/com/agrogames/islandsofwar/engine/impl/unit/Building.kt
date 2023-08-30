package com.agrogames.islandsofwar.engine.impl.unit

import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.map.MapObject
import com.agrogames.islandsofwar.engine.abs.map.MapProvider
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon

class Building(texture: String, location: Cell, weapons: Array<IWeapon>, health: Int) : LandUnit(texture, location, weapons, health, 0f, 0f) {
    override fun rotate(deltaTime: Float) {}
    override fun move(all: Array<MapObject>, deltaTime: Float) {}
    override fun think(provider: MapProvider) {}
}