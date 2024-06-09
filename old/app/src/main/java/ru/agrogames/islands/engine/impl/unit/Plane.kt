package ru.agrogames.islands.engine.impl.unit

import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.bullet.IBullet
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.engine.abs.weapon.IWeapon
import ru.agrogames.islands.engine.impl.bullet.Bullet
import ru.agrogames.islands.map.MapParams
import java.util.Stack

class Plane(texture: String, health: Int, speed: Float, rotationSpeed: Float, private val bombCount: Int, private val bombTexture: String, private val bombPower: Int, private val bombRange: Float) : Unit(texture, Cell(-1, -1), arrayOf<IWeapon>(), health, speed, rotationSpeed) {
    private var goal: Cell? = null
    private var direction: Direction? = null
    private var bombs: Stack<IBullet>? = null
    private var timeFromLastBomb: Float? = null
    override val territory: Array<Cell>
        get() {
            val l = Cell(location)
            return arrayOf(
                    l,
                    Cell(l.x, l.y + 1),
                    Cell(l.x, l.y - 1),
                    Cell(l.x + 1, l.y),
                    Cell(l.x + 2, l.y + 1),
                    Cell(l.x + 2, l.y - 1),
                    Cell(l.x - 1, l.y),
                    Cell(l.x - 1, l.y + 1),
                    Cell(l.x - 1, l.y - 1))
        }
    override val isMoving: Boolean
        get() = true
    override val height: Int
        get() = 4

    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        if (goal == null) return
        if (timeFromLastBomb != null) {
            bomb(bulletAdder)
            timeFromLastBomb = timeFromLastBomb!! + deltaTime
        }
        if (direction == Direction.Right) location.x += speed * deltaTime else if (direction == Direction.Left) location.x -= speed * deltaTime else if (direction == Direction.Up) location.y += speed * deltaTime else if (direction == Direction.Down) location.y -= speed * deltaTime
        val l = Cell(location)
        if (l == goal && timeFromLastBomb == null) timeFromLastBomb = 0.4f
    }

    private fun bomb(bulletAdder: BulletAdder) {
        if (bombs == null) {
            bombs = Stack()
            for (i in 0 until bombCount) {
                val b: IBullet = Bullet(bombTexture, null, location, 0.4f, bombPower, 0.1f, 2, 1, this, true, bombRange)
                bombs!!.push(b)
            }
        }
        if (timeFromLastBomb!! >= 0.4f && !bombs!!.isEmpty()) {
            timeFromLastBomb = timeFromLastBomb!! - 0.4f
            val b = bombs!!.pop()
            when (direction) {
                Direction.Up -> b.setGoal(Point(location.x, location.y + 0.5f))
                Direction.Right -> b.setGoal(Point(location.x + 0.5f, location.y))
                Direction.Down -> b.setGoal(Point(location.x, location.y - 0.5f))
                Direction.Left -> b.setGoal(Point(location.x - 0.5f, location.y))
                else -> {}
            }
            bulletAdder.addBullet(b)
        }
    }

    override fun cleverSetGoal(goal: Cell) {
        if (this.goal != null) return
        var gx = goal.x
        var gy = goal.y
        val cdx = gx - MapParams.width / 2
        val cdy = gy - MapParams.height / 2
        if (M.module(cdx) > M.module(cdy)) {
            if (cdx > 0) {
                gx = MapParams.width
                direction = Direction.Left
                rotation = Math.PI.toFloat()
            } else {
                direction = Direction.Right
                rotation = 0f
                gx = 1
            }
        } else {
            if (cdy > 0) {
                gy = MapParams.height
                direction = Direction.Down
                rotation = Math.PI.toFloat() / 2f * 3f
            } else {
                direction = Direction.Up
                rotation = Math.PI.toFloat() / 2f
                gy = 1
            }
        }
        location = Point(Cell(gx, gy))
        this.goal = goal
    }

    override val route
        get() = listOf(
                Cell(location),
                goal!!
        )

    private enum class Direction {
        Up, Right, Down, Left
    }
}