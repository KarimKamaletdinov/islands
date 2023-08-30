package com.agrogames.islandsofwar.engine.abs.movable

import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.unit.IUnit

interface MovableObject : IUnit {

    fun cleverSetGoal(goal: Cell)
    val route: Collection<Cell>
}