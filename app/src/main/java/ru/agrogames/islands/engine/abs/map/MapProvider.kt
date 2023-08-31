package ru.agrogames.islands.engine.abs.map

import ru.agrogames.islands.engine.abs.unit.IUnit

interface MapProvider {
    val our: Array<IUnit>
    val enemies: Array<IUnit>
    val all: Array<MapObject>
}