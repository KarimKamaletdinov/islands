package com.agrogames.islandsofwar.engine.abs.transport

import com.agrogames.islandsofwar.engine.abs.common.Cell

interface Transport {
    val units: List<TransportUnit>
    fun spawn(unit: TransportUnit, goal: Cell)
}