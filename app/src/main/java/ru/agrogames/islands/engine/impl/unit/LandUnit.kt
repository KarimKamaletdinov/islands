package ru.agrogames.islands.engine.impl.unit

import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.engine.abs.weapon.IWeapon
import ru.agrogames.islands.engine.impl.navigator.Navigator
import ru.agrogames.islands.map.Water
import java.util.Arrays
import java.util.Collections
import java.util.Stack
import kotlin.math.sqrt

open class LandUnit(texture: String, location: Cell, weapons: Array<IWeapon>, health: Int, speed: Float, rotationSpeed: Float) : Unit(texture, location, weapons, health, speed, rotationSpeed) {
    private var goalUnit: IUnit? = null
    override val route = Stack<Cell>()
    private var goal: Cell? = null
    override var isMoving = false
    private var myMaxDamage = weapons.maxOf { it.damage }


    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        think(provider)
        rotate(deltaTime)
        move(provider.all, deltaTime)
        for (weapon in weapons) {
            weapon.update(provider, bulletAdder, unitAdder, graphicsAdder, deltaTime)
        }
    }

    override fun cleverSetGoal(goal: Cell) {
        this.goal = goal
    }

    private fun buildRoute(all: Array<MapObject>, goal: Cell) {
        val map: MutableList<Cell> = ArrayList()
        for (`object` in all) {
            if (`object` !== this) {
                Collections.addAll(map, *`object`.territory)
            }
        }
        map.remove(goal)
        val r = Navigator.buildRoute(location, goal, map.toTypedArray())
        route.clear()
        for (i in r.indices.reversed()) {
            val c = r[i]
            route.push(c)
        }
    }

    protected open fun think(provider: MapProvider) {
        if (goal != null) {
            if (Arrays.stream(provider.all)
                            .noneMatch { o: MapObject? -> o is Water && o.territory[0] == goal }) {
                buildRoute(provider.all, goal!!)
            }
            goal = null
        }
        if (route.isEmpty()) {
            for (e in provider.enemies) {
                val weapon = Arrays.stream(e.weapons)
                        .max(Comparator.comparingInt { it.damage }).orElse(null)
                if (weapon != null && getDist(Point(territory[0]), weapon.location) <= weapon.longRange + 3 && weapon.damage >= health.current) {
                    cleverSetGoal(Cell(Point(location.x - (weapon.location.x - location.x) / 5f, location.y - (weapon.location.y - location.y) / 2f)))
                }
            }
            if (goalUnit == null) {
                var closest: IUnit? = null
                var distToClosest: Float? = null
                for (enemy in provider.enemies) {
                    if (enemy.minDamage > myMaxDamage) continue
                    val dist = getDist(location, enemy.location)
                    if (distToClosest == null) {
                        closest = enemy
                        distToClosest = dist
                    } else {
                        if (dist < distToClosest) {
                            closest = enemy
                            distToClosest = dist
                        }
                    }
                }
                if (closest != null) {
                    if (distToClosest!! > 5) {
                        goalUnit = closest
                        buildRoute(provider.all, goalUnit!!.territory[0])
                        setRotation(Cell(goalUnit!!.location))
                    }
                }
            } else {
                if (getDist(location, goalUnit!!.location) <= 5) {
                    goalUnit = null
                }
            }
        } else if (goalUnit != null) {
            if (getDist(location, goalUnit!!.location) <= 5) {
                goalUnit = null
                route.clear()
            }
        }
    }

    private fun getDist(p1: Point, p2: Point?): Float {
        val dx = p2!!.x - p1.x
        val dy = p2.y - p1.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    protected open fun move(all: Array<MapObject>, deltaTime: Float) {
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
            stop(nextCell, all)
            return
        }
        if (isTaken(furtherCell, all)) {
            stop(furtherCell, all)
            return
        }
        location = next
    }

    private fun stop(taken: Cell, all: Array<MapObject>) {
        var isTakingMoving = false
        for (`object` in all) {
            if (`object` !== this) {
                for (cell in `object`.territory) {
                    if (cell === taken) {
                        isTakingMoving = `object`.isMoving
                    }
                }
            }
        }
        if (isTakingMoving) {
            isMoving = false
        } else {
            if (!route.isEmpty()) buildRoute(all, route.firstElement())
        }
    }

    private fun isTaken(cell: Cell, all: Array<MapObject>): Boolean {
        for (`object` in all) {
            if (`object` !== this) {
                val territory = `object`.territory
                for (c in territory) {
                    if (cell == c) {
                        return true
                    }
                }
            }
        }
        return false
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