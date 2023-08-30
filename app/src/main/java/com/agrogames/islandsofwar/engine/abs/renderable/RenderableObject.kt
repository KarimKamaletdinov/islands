package com.agrogames.islandsofwar.engine.abs.renderable

import com.agrogames.islandsofwar.engine.abs.common.Point

interface RenderableObject {
    val location: Point
    val rotation: Float
    val texture: String
    fun timeSinceDestroyed(): Float {
        return 0f
    }
}