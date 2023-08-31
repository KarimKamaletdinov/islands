package ru.agrogames.islands.engine.abs.weapon

import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.abs.updatable.UpdatableObject

interface IWeapon : RenderableObject, UpdatableObject {
    fun setOwner(owner: IUnit)
    val relativeLocation: Point
    val bulletStarts: Array<Point>
    val longRange: Float
    val damage: Int
}