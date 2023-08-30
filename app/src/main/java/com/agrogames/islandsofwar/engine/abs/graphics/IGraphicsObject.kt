package com.agrogames.islandsofwar.engine.abs.graphics

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject

interface IGraphicsObject : RenderableObject, UpdatableObject {
    fun shouldBeRemoved(): Boolean
    val spawnSound: String?
}