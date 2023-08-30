package com.agrogames.islandsofwar.engine.abs.unit

interface IUnitAdder {
    fun addUnit(unit: IUnit)
    val units: List<IUnit>
}