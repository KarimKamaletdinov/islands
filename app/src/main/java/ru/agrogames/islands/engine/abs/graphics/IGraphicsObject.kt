package ru.agrogames.islands.engine.abs.graphics

import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.updatable.UpdatableObject

interface IGraphicsObject : RenderableObject, UpdatableObject {
    fun shouldBeRemoved(): Boolean
    val spawnSound: String?
}