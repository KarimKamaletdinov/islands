package com.agrogames.islandsofwar.engine.impl.weapon

import com.agrogames.islandsofwar.common.M
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet
import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder
import com.agrogames.islandsofwar.engine.abs.map.MapObject
import com.agrogames.islandsofwar.engine.abs.map.MapProvider
import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon
import com.agrogames.islandsofwar.engine.impl.bullet.Bullet

class Weapon(override val relativeLocation: Point, private val reload: Float, override val texture: String, override val longRange: Float,
             private val rotationSpeed: Float, private val bulletsStarts: Array<Point?>?, private val bulletTexture: String, damage: Int, private val speed: Float, private val flightHeight: Int, private val targetHeight: Int, private val bang: Boolean, private val bangRange: Float, private val bulletCreationSound: String) : IWeapon {
    override val damage: Int
    private var fromLastReload = 0f
    private var relativeRotation = 0f
    private var goalRotation: Float? = null
    private lateinit var owner: IUnit

    init {
        this.damage = damage * bulletsStarts!!.size
    }

    override val location: Point
        get() {
            val rr = relativeLocation.rotate(owner.rotation)
            return Point(owner.location.x + rr.x, owner.location.y + rr.y)
        }
    override val bulletStarts: Array<Point>
        get() {
            val result: MutableList<Point> = ArrayList()
            val l = location
            val rotation = rotation
            for (s in bulletsStarts!!) {
                val rr = s!!.rotate(rotation)
                result.add(Point(l.x + rr.x, l.y + rr.y))
            }
            return result.toTypedArray()
        }
    override val rotation: Float
        get() {
            var r = owner.rotation + relativeRotation
            if (r > Math.PI * 2f) {
                r -= (Math.PI * 2f).toFloat()
            }
            if (r < 0) {
                r += (Math.PI * 2f).toFloat()
            }
            return r
        }

    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        val near = provider.enemies.filter { isNear(it) && isAvailable(it, provider.all) }
                .sortedBy {getDist(it.location, location).toInt() }
        for (enemy in near) {
            setRotation(enemy)
            break
        }
        rotate(deltaTime)
        fromLastReload += deltaTime
        if (fromLastReload < reload) return
        for (enemy in near) {
            setRotation(enemy)
            shoot(bulletAdder, enemy)
            return
        }
    }

    private fun shoot(bulletAdder: BulletAdder, enemy: IUnit) {
        if (!M.nearlyEquals(rotation, goalRotation!!)) return
        fromLastReload = 0f
        val enemyLocation = if (enemy.territory.size == 0) enemy.location else Point(enemy.territory[0])
        for (start in bulletStarts) {
            val bullet: IBullet = Bullet(bulletTexture, bulletCreationSound, start, speed, damage, longRange,
                    flightHeight, targetHeight, owner, bang, bangRange)
            bullet.setGoal(enemyLocation)
            bulletAdder.addBullet(bullet)
        }
    }

    private fun setRotation(enemy: IUnit) {
        val location = location
        val enemyLocation = enemy.location
        goalRotation = getAngle(enemyLocation, location)
        if (goalRotation!! > Math.PI * 2f) {
            goalRotation = goalRotation!! - (Math.PI * 2f).toFloat()
        }
        if (goalRotation!! < 0) {
            goalRotation = goalRotation!! + Math.PI.toFloat() * 2f
        }
    }

    private fun rotate(deltaTime: Float) {
        if (goalRotation == null) return
        var r = rotation
        if (r > Math.PI * 2f) {
            r -= (Math.PI * 2f).toFloat()
        }
        if (r < 0) {
            r += (Math.PI * 2f).toFloat()
        }
        if (r == goalRotation) return
        if (r - goalRotation!! > Math.PI) {
            r -= (Math.PI * 2f).toFloat()
        } else if (goalRotation!! - r > Math.PI) {
            r += (Math.PI * 2f).toFloat()
        }
        if (r < goalRotation!!) {
            r += (Math.PI * deltaTime * rotationSpeed).toFloat()
            if (r > goalRotation!!) {
                r = goalRotation!!
            }
        } else {
            r -= (Math.PI * deltaTime * rotationSpeed).toFloat()
            if (r < goalRotation!!) {
                r = goalRotation!!
            }
        }
        relativeRotation = r - owner.rotation
    }

    private fun isAvailable(enemy: IUnit, all: Array<MapObject>): Boolean {
        if (enemy.minDamage > damage || enemy.height != targetHeight) return false
        val location = location
        val enemyLocation = enemy.location
        var r = getAngle(enemyLocation, location)
        if (r > Math.PI * 2f) {
            r -= (Math.PI * 2f).toFloat()
        }
        if (r < 0) {
            r += (Math.PI * 2f).toFloat()
        }
        val d = getDist(enemyLocation, location)
        var l = 0f
        var x = location.x
        var y = location.y
        while (l < d) {
            l += 0.5f
            x += (Math.cos(r.toDouble()) * 0.5).toFloat()
            y += (Math.sin(r.toDouble()) * 0.5).toFloat()
            for (mapObject in all) {
                if (mapObject !== owner && mapObject !== enemy && mapObject.height == flightHeight) {
                    for (cell in mapObject.territory) {
                        val c = Cell(x.toInt() + 1, y.toInt() + 1)
                        if (cell == c) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    private fun getAngle(p1: Point?, p2: Point?): Float {
        var r = Math.atan(
                (p1!!.y.toDouble() - p2!!.y.toDouble()) /
                        (p1.x.toDouble() - p2.x.toDouble())).toFloat()
        if (p1.x < p2.x) {
            r += Math.PI.toFloat()
        }
        return r
    }

    private fun isNear(enemy: IUnit): Boolean {
        val el = enemy.location
        val l = location
        val dist = getDist(el, l)
        return dist <= longRange
    }

    private fun getDist(p1: Point?, p2: Point?): Float {
        val dx = p1!!.x - p2!!.x
        val dy = p1.y - p2.y
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    override fun setOwner(owner: IUnit) {
        this.owner = owner
    }
}