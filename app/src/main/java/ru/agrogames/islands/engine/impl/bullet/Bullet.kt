package ru.agrogames.islands.engine.impl.bullet

import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.bullet.IBullet
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.factories.Factory
import ru.agrogames.islands.map.Water

class Bullet(override val texture: String, override val creationSound: String?, override var location: Point, private val speed: Float, private val power: Int, private val longRange: Float, private val flightHeight: Int, private val targetHeight: Int, private val owner: IUnit?, private val bang: Boolean, private val bangRange: Float) : IBullet {
    override var rotation = 0f
        private set
    private var isFlying = true
    private var flewDistance = 0f
    private var goal: Cell? = null
    override fun hasStopped(): Boolean {
        return !isFlying
    }

    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        move(deltaTime)
        val all = provider.all
        val enemies = enemyAt(Cell(location.x.toInt() + 1, location.y.toInt() + 1), all, flightHeight)
        if (enemies.size != 0) {
            for (enemy in enemies) {
                if (enemy is IUnit) enemy.loseHealth(power)
                stop(provider, graphicsAdder)
                graphicsAdder.addGraphics(Factory.getGraphics("hit", location, rotation))
            }
        } else if (Cell(location.x.toInt() + 1, location.y.toInt() + 1) == goal || flewDistance >= longRange) {
            val e2 = enemyAt(Cell(location.x.toInt() + 1, location.y.toInt() + 1), all, targetHeight)
            for (enemy in e2) {
                if (enemy is IUnit) enemy.loseHealth(power)
                graphicsAdder.addGraphics(Factory.getGraphics("hit", location, rotation))
            }
            stop(provider, graphicsAdder)
        }
    }

    private fun stop(provider: MapProvider, adder: GraphicsAdder) {
        isFlying = false
        if (bang && provider.all.none { it.territory.contains(Cell(location)) && it is Water } && targetHeight == 1) {
            adder.addGraphics(Factory.getGraphics("bang", location, rotation))
        }
    }

    private fun move(deltaTime: Float) {
        val dx = Math.cos(rotation.toDouble()) * speed.toDouble() * deltaTime.toDouble()
        val dy = Math.sin(rotation.toDouble()) * speed.toDouble() * deltaTime.toDouble()
        location = Point(location.x + dx.toFloat(), location.y + dy.toFloat())
        flewDistance += speed * deltaTime
    }

    private fun enemyAt(cell: Cell, enemies: Array<MapObject>, height: Int): Array<MapObject?> {
        if (bang) {
            val e: MutableList<MapObject?> = ArrayList()
            for (`object` in enemies) {
                if (`object` !== owner && `object`.height == height) {
                    val territory = `object`.territory
                    for (c in territory) {
                        if (cell == c) {
                            if (!e.contains(`object`)) e.add(`object`)
                        }
                    }
                    if (`object` is RenderableObject) {
                        val l = (`object` as RenderableObject).location
                        if (l.x - bangRange < location.x && l.x + bangRange > location.x && l.y - bangRange < location.y && l.y + bangRange > location.y) {
                            if (!e.contains(`object`)) e.add(`object`)
                        }
                    }
                }
            }
            return e.toTypedArray()
        } else {
            for (`object` in enemies) {
                if (`object` !== owner && `object`.height == height) {
                    val territory = `object`.territory
                    for (c in territory!!) {
                        if (cell == c) {
                            return arrayOf(`object`)
                        }
                    }
                }
            }
        }
        return arrayOfNulls(0)
    }

    override fun setGoal(goal: Point?) {
        rotation = Math.atan(
                (goal!!.y.toDouble() - location.y.toDouble()) /
                        (goal.x.toDouble() - location.x.toDouble())).toFloat()
        if (goal.x < location.x) {
            rotation += Math.PI.toFloat()
        }
        this.goal = Cell(goal)
    }
}