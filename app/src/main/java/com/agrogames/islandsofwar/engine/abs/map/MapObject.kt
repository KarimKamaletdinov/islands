package com.agrogames.islandsofwar.engine.abs.map

import com.agrogames.islandsofwar.engine.abs.common.Cell

interface MapObject {
    val territory: Array<Cell>
    val isMoving: Boolean
    val height: Int
}