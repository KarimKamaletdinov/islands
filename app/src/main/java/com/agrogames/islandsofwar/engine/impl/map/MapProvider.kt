package com.agrogames.islandsofwar.engine.impl.map

import com.agrogames.islandsofwar.engine.abs.map.MapObject
import com.agrogames.islandsofwar.engine.abs.map.MapProvider
import com.agrogames.islandsofwar.engine.abs.unit.IUnit

class MapProvider(override val our: Array<IUnit>, override val enemies: Array<IUnit>, mapObjects: Array<MapObject>) : MapProvider {
    override val all: Array<MapObject>

    init {
        val all  = mutableListOf<MapObject>(*our)
        all.addAll(enemies)
        all.addAll(mapObjects)
        this.all = all.toTypedArray()
    }
}