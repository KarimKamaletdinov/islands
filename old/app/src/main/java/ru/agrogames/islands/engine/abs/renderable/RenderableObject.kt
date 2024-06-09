package ru.agrogames.islands.engine.abs.renderable

import ru.agrogames.islands.engine.abs.common.Point

interface RenderableObject {
    val location: Point
    val rotation: Float
    val texture: String
    fun timeSinceDestroyed(): Float {
        return 0f
    }
}