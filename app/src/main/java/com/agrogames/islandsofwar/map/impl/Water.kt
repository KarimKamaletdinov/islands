package com.agrogames.islandsofwar.map.impl

import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.map.MapObject

class Water(private val location: Cell) : MapObject {
    override val territory: Array<Cell>
        get() = arrayOf(location)
    override val isMoving: Boolean
        get() = false
    override val height: Int
        get() = 0
}