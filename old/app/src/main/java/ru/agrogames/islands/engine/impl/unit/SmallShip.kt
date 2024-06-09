package ru.agrogames.islands.engine.impl.unit

import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.movable.MovableObject
import ru.agrogames.islands.engine.abs.transport.TransportUnit
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.engine.abs.weapon.IWeapon
import ru.agrogames.islands.engine.impl.navigator.Navigator
import ru.agrogames.islands.map.MapParams
import ru.agrogames.islands.map.Water
import java.util.Collections
import java.util.Stack

class SmallShip(texture: String, location: Cell, weapons: Array<IWeapon>, health: Int, speed: Float, rotationSpeed: Float)
    : Unit(texture, location, weapons, health, speed, rotationSpeed) {
    override val route = Stack<Cell>()
    var goal: Cell? = null
    override var isMoving = false
        private set
    var unit: TransportUnit? = null

    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        if (goal != null) {
            buildRoute(provider.all, goal)
            goal = null
        }
        rotate(deltaTime)
        move(provider.all, unitAdder, deltaTime)
        for (weapon in weapons) {
            weapon.update(provider, bulletAdder, unitAdder, graphicsAdder, deltaTime)
        }
    }

    override fun cleverSetGoal(goal: Cell) {
        this.goal = goal
    }

    private fun buildRoute(all: Array<MapObject>, goal: Cell?) {
        val map: MutableList<Cell> = ArrayList()
        for (mo in all) {
            if ( mo !is SmallShip) {
                for (c in mo.territory) {
                    map.remove(c)
                }
            }
        }
        map.remove(goal)
        val r = Navigator.buildRoute(location, goal!!, map.toTypedArray())
        route.clear()
        for (i in r.indices.reversed()) {
            val c = r[i]
            route.push(c)
        }
    }

    private fun move(all: Array<MapObject>, adder: IUnitAdder, deltaTime: Float) {
        if (route.isEmpty()) {
            isMoving = false
            return
        }
        if (Cell(location) == route.lastElement()) {
            route.pop()
        }
        if (route.isEmpty()) {
            isMoving = false
            return
        }
        val g = route.lastElement()
        setRotation(g!!)
        val next = next(deltaTime, g)
        val nextCell = Cell(next)
        val further = next(0.5f / speed, g)
        val furtherCell = Cell(further)
        if (isTaken(nextCell, all)) {
            stop(nextCell, all, adder)
            return
        }
        if (isTaken(furtherCell, all)) {
            stop(furtherCell, all, adder)
            return
        }
        location = next
    }

    private fun stop(taken: Cell, all: Array<MapObject>, adder: IUnitAdder) {
        var isTakingMoving = false
        var stoppedOnUnit = false
        for (`object` in all) {
            for (cell in `object`.territory) {
                if (cell == taken) {
                    if (`object` !== this && `object` is SmallShip) {
                        isTakingMoving = `object`.isMoving
                    }
                    stoppedOnUnit = true
                }
            }
        }
        if (!stoppedOnUnit) {
            land(adder, taken)
        }
        if (isTakingMoving) {
            isMoving = false
        } else {
            if (!route.isEmpty()) buildRoute(all, route.firstElement())
        }
    }

    private fun land(adder: IUnitAdder, cell: Cell) {
        if (unit == null) return
        val u = unit!!.create.apply(cell)
        if (!route.isEmpty() && cell != route.firstElement()) {
            (u as MovableObject).cleverSetGoal(route.firstElement())
        }
        adder.addUnit(u)
        unit = null
        health.current = 0
        timeSinceDestroyed = 100f
    }

    private fun isTaken(cell: Cell, all: Array<MapObject>): Boolean {
        val map: MutableList<Cell?> = ArrayList()
        for (x in 0 until MapParams.width) {
            for (y in 0 until MapParams.height) {
                map.add(Cell(x, y))
            }
        }
        for (`object` in all) {
            if (`object` is Water) {
                for (c in `object`.territory) {
                    map.remove(c)
                }
            } else if (`object` !== this) {
                Collections.addAll(map, *`object`.territory)
            }
        }
        return map.contains(cell)
    }

    private fun next(deltaTime: Float, goal: Cell?): Point {
        val g = Point(goal)
        val lx = (g.x - location.x).toDouble()
        val ly = (g.y - location.y).toDouble()
        val l = Math.sqrt(lx * lx + ly * ly)
        val rel = l / (speed * deltaTime)
        var dx = lx / rel
        var dy = ly / rel
        if (M.module(dx) > M.module(lx)) {
            dx = lx
            dy = ly
        }
        if (java.lang.Double.isNaN(dx)) {
            dx = 0.0
        }
        if (java.lang.Double.isNaN(dy)) {
            dy = 0.0
        }
        return Point(location.x + dx.toFloat(), location.y + dy.toFloat())
    }

    override val height: Int
        get() = 1
}