package ru.agrogames.islands.engine.impl.unit

import android.util.Pair
import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.movable.MovableObject
import ru.agrogames.islands.engine.abs.transport.Transport
import ru.agrogames.islands.engine.abs.transport.TransportUnit
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.engine.abs.weapon.IWeapon
import ru.agrogames.islands.engine.impl.navigator.BigShipNavigator
import ru.agrogames.islands.factories.Factory
import ru.agrogames.islands.map.MapParams
import ru.agrogames.islands.map.Water
import java.util.Collections
import java.util.Stack

class BigShip(texture: String, location: Cell, weapons: Array<IWeapon>, health: Int, speed: Float, rotationSpeed: Float, units: Array<TransportUnit>, minDamage: Int)
    : Unit(texture, location, weapons, health, speed, rotationSpeed), Transport {
    override val route = Stack<Cell>()
    override val units: MutableList<TransportUnit> = ArrayList()
    private val toAdd: MutableList<Pair<TransportUnit, Cell>> = ArrayList()

    init {
        this.minDamage = minDamage
        Collections.addAll(this.units, *units)
    }

    override val isMoving: Boolean
        get() = false
    override val height: Int
        get() = 1


    override fun cleverSetGoal(goal: Cell) {
        var gx = goal.x
        var gy = goal.y
        if (goal.x != 1 && goal.x != MapParams.width && goal.y != 1 && goal.y != MapParams.height) {
            val cdx = gx - MapParams.width / 2
            val cdy = gy - MapParams.height / 2
            if (M.module(cdx) > M.module(cdy)) {
                gx = if (cdx > 0) {
                    MapParams.width
                } else {
                    1
                }
            } else {
                gy = if (cdy > 0) {
                    MapParams.height
                } else {
                    1
                }
            }
        }
        if (goal.x < 1) gx = 1
        if (goal.y < 1) gy = 1
        if (goal.x > MapParams.width) gx = MapParams.width
        if (goal.y > MapParams.height) gy = MapParams.height
        val r = BigShipNavigator.buildRoute(Cell(location), Cell(gx, gy))
        route.clear()
        for (i in r.indices.reversed()) {
            val c = r[i]
            route.push(c)
        }
    }


    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        for (weapon in weapons) {
            weapon.update(provider, bulletAdder, unitAdder, graphicsAdder, deltaTime)
        }
        for (o in toAdd.toTypedArray()) {
            val l = Cell(location)
            val c = Cell(l.x + 1, l.y + 1)
            if (provider.all.none { it.territory.contains(c) && it !is Water }) {
                val lc = Factory["landing_craft", l.x + 1, l.y + 1]
                (lc as SmallShip).unit = o.first
                (lc as MovableObject).cleverSetGoal(o.second)
                unitAdder.addUnit(lc)
                toAdd.remove(o)
            }
            break
        }
        if (route.isEmpty()) return
        if (Point(route.lastElement()) == location) route.pop()
        if (route.isEmpty()) return
        val g = Point(route.lastElement())
        if (g.x > location.x) {
            location.x += speed * deltaTime
            if (location.x > g.x) location.x = g.x
            goalRotation = 0f
        } else if (g.x < location.x) {
            location.x -= speed * deltaTime
            if (location.x < g.x) location.x = g.x
            goalRotation = -Math.PI.toFloat()
        } else if (g.y > location.y) {
            location.y += speed * deltaTime
            if (location.y > g.y) location.y = g.y
            goalRotation = Math.PI.toFloat() / 2f
        } else if (g.y < location.y) {
            location.y -= speed * deltaTime
            if (location.y < g.y) location.y = g.y
            goalRotation = -Math.PI.toFloat() / 2f
        }
        if (route.lastElement() != Cell(location)) {
            rotate(deltaTime)
        }
    }

    override fun spawn(unit: TransportUnit, goal: Cell) {
        if (units.remove(unit)) {
            toAdd.add(Pair(unit, goal))
        }
    }

    override val territory: Array<Cell>
        get() {
            val l = Cell(location)
            val r = rotation
            val g45 = Math.PI.toFloat() / 4.0f
            return if (r < g45 || r > g45 * 3 && r < g45 * 5 || r > g45 * 7) {
                arrayOf(
                        l,
                        Cell(l.x + 2, l.y),
                        Cell(l.x + 1, l.y),
                        Cell(l.x - 1, l.y),
                        Cell(l.x - 2, l.y))
            } else arrayOf(
                    l,
                    Cell(l.x, l.y + 2),
                    Cell(l.x, l.y + 1),
                    Cell(l.x, l.y - 1),
                    Cell(l.x, l.y - 2))
        }
}