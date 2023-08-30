package com.agrogames.islandsofwar.engine.abs.map

import com.agrogames.islandsofwar.engine.abs.unit.IUnit

interface MapProvider {
    val our: Array<IUnit>
    val enemies: Array<IUnit>
    val all: Array<MapObject>
}