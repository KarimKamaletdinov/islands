package ru.agrogames.islands.map

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.map.MapObject

class Water(private val location: Cell) : MapObject {
    override val territory: Array<Cell>
        get() = arrayOf(location)
    override val isMoving: Boolean
        get() = false
    override val height: Int
        get() = 0
}