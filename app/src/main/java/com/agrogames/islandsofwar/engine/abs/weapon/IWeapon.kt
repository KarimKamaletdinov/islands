package com.agrogames.islandsofwar.engine.abs.weapon

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject
import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject

interface IWeapon : RenderableObject, UpdatableObject {
    fun setOwner(owner: IUnit)
    val relativeLocation: Point
    val bulletStarts: Array<Point>
    val longRange: Float
    val damage: Int
}