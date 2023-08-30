package com.agrogames.islandsofwar.engine.impl.navigator

import com.agrogames.islandsofwar.engine.abs.common.Cell
import java.util.Objects

class RouteCell(val cell: Cell) {
    val triedDirections: MutableList<Direction?> = ArrayList()
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val routeCell = o as RouteCell
        return cell == routeCell.cell
    }

    override fun hashCode(): Int {
        return Objects.hash(cell)
    }
}