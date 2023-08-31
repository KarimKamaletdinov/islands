package ru.agrogames.islands.islands.abs

import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.map.Map

class Island(val id: Int, val map: Map, val owners: Array<IUnit>, val attacker: IUnit)