package ru.agrogames.islands.engine.abs.transport

import ru.agrogames.islands.engine.abs.common.Cell

interface Transport {
    val units: List<TransportUnit>
    fun spawn(unit: TransportUnit, goal: Cell)
}