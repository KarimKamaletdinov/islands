package com.agrogames.islandsofwar.engine.impl.unit

import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder
import com.agrogames.islandsofwar.sounds.SoundPlayer

class UnitAdder() : IUnitAdder {
    override val units: MutableList<IUnit> = ArrayList()
    override fun addUnit(unit: IUnit) {
        units.add(unit)
        SoundPlayer.playSound("landing")
    }
}