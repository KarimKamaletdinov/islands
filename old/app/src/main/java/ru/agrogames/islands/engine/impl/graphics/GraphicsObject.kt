package ru.agrogames.islands.engine.impl.graphics

import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.graphics.IGraphicsObject
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.unit.IUnitAdder

class GraphicsObject(override val texture: String, override val location: Point, override val rotation: Float, override val spawnSound: String, private val lifeTime: Float, private val next: IGraphicsObject? = null) : IGraphicsObject {
    private var timeSinceCreated = 0f
    private var remove = false
    override fun shouldBeRemoved(): Boolean {
        return remove
    }

    override fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float) {
        timeSinceCreated += deltaTime
        if (timeSinceCreated >= lifeTime && !remove) {
            remove = true
            if (next != null) {
                graphicsAdder.addGraphics(next)
            }
        }
    }
}