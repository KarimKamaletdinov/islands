package ru.agrogames.islands.engine.abs.unit

import ru.agrogames.islands.engine.abs.gamevalue.IntValue
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.updatable.UpdatableObject
import ru.agrogames.islands.engine.abs.weapon.IWeapon

interface IUnit : MapObject, RenderableObject, UpdatableObject {
    val health: IntValue
    val minDamage: Int
    val weapons: Array<IWeapon>
    fun loseHealth(lost: Int)
    fun addTsd(deltaTime: Float)
}