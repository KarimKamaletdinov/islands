package com.agrogames.islandsofwar.engine.abs.unit

import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue
import com.agrogames.islandsofwar.engine.abs.map.MapObject
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon

interface IUnit : MapObject, RenderableObject, UpdatableObject {
    val health: IntValue
    val minDamage: Int
    val weapons: Array<IWeapon>
    fun loseHealth(lost: Int)
    fun addTsd(deltaTime: Float)
}