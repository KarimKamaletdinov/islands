package ru.agrogames.islands.engine.impl.navigator

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.map.MapParams

object BigShipNavigator {
    fun buildRoute(start: Cell, finish: Cell): List<Cell> {
        if (start.x != 1 && start.y != 1 && start.x != MapParams.width && start.y != MapParams.height ||
                finish.x != 1 && finish.y != 1 && finish.x != MapParams.width && finish.y != MapParams.height) return ArrayList()
        val availableDirections: MutableList<Direction> = ArrayList()
        availableDirections.add(Direction.Top)
        availableDirections.add(Direction.Right)
        availableDirections.add(Direction.Bottom)
        availableDirections.add(Direction.Left)
        if (start.y == MapParams.height) {
            availableDirections.remove(Direction.Top)
            if (start.x > 1 && start.x < MapParams.width) {
                availableDirections.remove(Direction.Bottom)
            }
        }
        if (start.x == MapParams.width) {
            availableDirections.remove(Direction.Right)
            if (start.y > 1 && start.y < MapParams.height) {
                availableDirections.remove(Direction.Left)
            }
        }
        if (start.y == 1) {
            availableDirections.remove(Direction.Bottom)
            if (start.x > 1 && start.x < MapParams.width) {
                availableDirections.remove(Direction.Top)
            }
        }
        if (start.x == 1) {
            availableDirections.remove(Direction.Left)
            if (start.y > 1 && start.y < MapParams.height) {
                availableDirections.remove(Direction.Right)
            }
        }
        val availableRoutes: MutableList<List<Cell>> = ArrayList()
        for (d in availableDirections) {
            var direction = d
            val route: MutableList<Cell> = ArrayList()
            var c = Cell(start.x, start.y)
            route.add(c)
            var attempt = 0
            while (c != finish && attempt < 500) {
                if (direction == Direction.Top) {
                    c = Cell(c.x, MapParams.height)
                    if (c.x == finish.x) c = Cell(c.x, finish.y)
                    route.add(c)
                    direction = if (c.x == 1) Direction.Right else Direction.Left
                } else if (direction == Direction.Bottom) {
                    c = Cell(c.x, 1)
                    if (c.x == finish.x) c = Cell(c.x, finish.y)
                    route.add(c)
                    direction = if (c.x == 1) Direction.Right else Direction.Left
                } else if (direction == Direction.Right) {
                    c = Cell(MapParams.width, c.y)
                    if (c.y == finish.y) c = Cell(finish.x, c.y)
                    route.add(c)
                    direction = if (c.y == 1) Direction.Top else Direction.Bottom
                } else {
                    c = Cell(1, c.y)
                    if (c.y == finish.y) c = Cell(finish.x, c.y)
                    route.add(c)
                    direction = if (c.y == 1) Direction.Top else Direction.Bottom
                }
                attempt++
            }
            availableRoutes.add(route)
        }
        availableRoutes.sortBy{ it.size }
        return availableRoutes[0]
    }
}