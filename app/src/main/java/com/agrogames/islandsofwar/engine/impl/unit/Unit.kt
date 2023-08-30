package com.agrogames.islandsofwar.engine.impl.unit

import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject
import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon
import kotlin.math.atan

abstract class Unit protected constructor(override val texture: String,
                                          location: Cell,
                                          final override val weapons: Array<IWeapon>,
                                          health: Int,
                                          val speed: Float,
                                          private val rotationSpeed: Float) : IUnit, MovableObject {
    override val health = IntValue(health)
    override var location = Point(location)
    override var rotation = 0f
        protected set
    protected var goalRotation = 0f
    override var minDamage = 0
        protected set
    protected var timeSinceDestroyed = 0f

    init {
        for (weapon in weapons) {
            weapon.setOwner(this)
        }
    }

    override fun loseHealth(lost: Int) {
        if (lost >= minDamage) {
            health.current = health.current!! - lost
        }
    }

    override fun addTsd(deltaTime: Float) {
        if (health.current!! <= 0) {
            timeSinceDestroyed += deltaTime
        }
    }

    override fun timeSinceDestroyed(): Float {
        return timeSinceDestroyed
    }

    protected open fun rotate(deltaTime: Float) {
        if (rotation == goalRotation) return
        if (rotation - goalRotation > Math.PI) {
            rotation -= (Math.PI * 2f).toFloat()
        } else if (goalRotation - rotation > Math.PI) {
            rotation += (Math.PI * 2f).toFloat()
        }
        if (rotation < goalRotation) {
            rotation += (Math.PI * deltaTime * rotationSpeed).toFloat()
            if (rotation > goalRotation) {
                rotation = goalRotation
            }
        } else {
            rotation -= (Math.PI * deltaTime * rotationSpeed).toFloat()
            if (rotation < goalRotation) {
                rotation = goalRotation
            }
        }
    }

    protected fun setRotation(goal: Cell) {
        if (goal == Cell(location)) return
        val g = Point(goal)
        goalRotation = atan(
                (g.y.toDouble() - location.y.toDouble()) /
                        (g.x.toDouble() - location.x.toDouble())).toFloat()
        if (g.x < location.x) {
            goalRotation += Math.PI.toFloat()
        }
        if (goalRotation > Math.PI * 2f) {
            goalRotation -= (Math.PI * 2f).toFloat()
        }
        if (goalRotation < 0) {
            goalRotation += Math.PI.toFloat() * 2f
        }
    }

    override val territory: Array<Cell>
        get() = arrayOf(
                Cell((location.x + 0.5f).toInt(), (location.y + 0.5f).toInt())
        )
}