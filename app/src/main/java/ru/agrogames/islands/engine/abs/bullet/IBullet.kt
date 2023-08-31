package ru.agrogames.islands.engine.abs.bullet

import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.updatable.UpdatableObject

interface IBullet : RenderableObject, UpdatableObject {
    fun setGoal(goal: Point?)
    fun hasStopped(): Boolean
    val creationSound: String?
}