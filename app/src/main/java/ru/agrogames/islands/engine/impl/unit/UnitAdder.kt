package ru.agrogames.islands.engine.impl.unit

import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.abs.unit.IUnitAdder
import ru.agrogames.islands.sounds.SoundPlayer

class UnitAdder() : IUnitAdder {
    override val units: MutableList<IUnit> = ArrayList()
    override fun addUnit(unit: IUnit) {
        units.add(unit)
        SoundPlayer.playSound("landing")
    }
}