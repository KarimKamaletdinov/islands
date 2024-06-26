package ru.agrogames.islands.engine.abs.transport

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.unit.IUnit
import java.util.Objects
import java.util.function.Function

class TransportUnit constructor(val create: Function<Cell, IUnit>) {
    val example = create.apply(Cell(-1, -1))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return example.texture == (other as TransportUnit).example.texture
    }

    override fun hashCode(): Int {
        return Objects.hash(example.texture)
    }
}