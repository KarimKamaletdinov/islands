package ru.agrogames.islands.engine.abs.map

import ru.agrogames.islands.engine.abs.common.Cell

interface MapObject {
    val territory: Array<Cell>
    val isMoving: Boolean
    val height: Int
}