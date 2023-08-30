package com.agrogames.islandsofwar.engine.impl.graphics

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder
import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject
import com.agrogames.islandsofwar.engine.abs.map.MapProvider
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder

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