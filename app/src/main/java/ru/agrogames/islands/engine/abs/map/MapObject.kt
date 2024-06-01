package ru.agrogames.islands.engine.abs.map

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.renderable.RenderableObject

interface MapObject {
    val territory: Array<Cell>
    val isMoving: Boolean
    val height: Int
}