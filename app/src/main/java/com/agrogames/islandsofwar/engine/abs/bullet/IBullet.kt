package com.agrogames.islandsofwar.engine.abs.bullet

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject

interface IBullet : RenderableObject, UpdatableObject {
    fun setGoal(goal: Point?)
    fun hasStopped(): Boolean
    val creationSound: String?
}