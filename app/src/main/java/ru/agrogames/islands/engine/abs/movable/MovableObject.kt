package ru.agrogames.islands.engine.abs.movable

import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.unit.IUnit

interface MovableObject : IUnit {

    fun cleverSetGoal(goal: Cell)
    val route: Collection<Cell>
}