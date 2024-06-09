package ru.agrogames.islands.engine.impl.navigator

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point

object Navigator {
    fun buildRoute(start: Point, finish: Cell, map: Array<Cell>): Array<Cell> {
        return buildSimpleRoute(Cell(start), finish, listOf(*map)).toTypedArray()
    }

    private fun buildSimpleRoute(start: Cell, finish: Cell, map: List<Cell>): List<Cell> {
        val simpleRoute: MutableList<RouteCell> = ArrayList()
        var location: RouteCell? = RouteCell(Cell(start.x, start.y))
        simpleRoute.add(location!!)
        if (location.cell == finish) return ArrayList()
        var attempt = 0
        var i = 0
        while (i < 30) {
            if (i < 0) {
                return ArrayList()
            }
            location = moveOptimally(location!!, finish, map, simpleRoute)
            if (location == null) {
                i -= 2
                if (simpleRoute.size > 1) {
                    simpleRoute.removeAt(simpleRoute.size - 1)
                    location = simpleRoute[simpleRoute.size - 1]
                }
            } else {
                simpleRoute.add(location)
                if (location.cell == finish) break
            }
            if (attempt >= 50) {
                return ArrayList()
            }
            attempt++
            i++
        }
        return simpleRoute.map { it.cell }
    }

    private fun moveOptimally(cell: RouteCell, goal: Cell, map: List<Cell>,
                              route: List<RouteCell>): RouteCell? {
        var c = tryMove(cell, Direction.primary(cell.cell, goal), map, route)
        if (c == null) c = tryMove(cell, Direction.secondary(cell.cell, goal), map, route)
        if (c == null) c = tryMove(cell, Direction.tertiary(cell.cell, goal), map, route)
        if (c == null) c = tryMove(cell, Direction.quaternary(cell.cell, goal), map, route)
        return c
    }

    private fun tryMove(cell: RouteCell, direction: Direction, map: List<Cell>,
                        route: List<RouteCell>): RouteCell? {
        if (cell.triedDirections.contains(direction)) return null
        val moved = move(cell.cell, direction)
        if (map.contains(moved)) {
            return null
        }
        val rc = RouteCell(moved)
        if (route.contains(rc)) return null
        cell.triedDirections.add(direction)
        return rc
    }

    private fun move(cell: Cell, direction: Direction): Cell {
        return when (direction) {
            Direction.Top -> Cell(cell.x, cell.y + 1)
            Direction.Right -> Cell(cell.x + 1, cell.y)
            Direction.Bottom -> Cell(cell.x, cell.y - 1)
            Direction.Left -> Cell(cell.x - 1, cell.y)
        }
    }
}