package ru.agrogames.islands.engine.impl.unit

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.weapon.IWeapon

class Building(texture: String, location: Cell, weapons: Array<IWeapon>, health: Int) : LandUnit(texture, location, weapons, health, 0f, 0f) {
    override fun rotate(deltaTime: Float) {}
    override fun move(all: Array<MapObject>, deltaTime: Float) {}
    override fun think(provider: MapProvider) {}
}