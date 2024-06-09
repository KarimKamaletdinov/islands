package ru.agrogames.islands.engine.impl.map

import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.unit.IUnit

class MapProvider(override val our: Array<IUnit>, override val enemies: Array<IUnit>, mapObjects: Array<MapObject>) : MapProvider {
    override val all: Array<MapObject>

    init {
        val all  = mutableListOf<MapObject>(*our)
        all.addAll(enemies)
        all.addAll(mapObjects)
        this.all = all.toTypedArray()
    }
}