package ru.agrogames.islands.engine.abs.unit

interface IUnitAdder {
    fun addUnit(unit: IUnit)
    val units: List<IUnit>
}