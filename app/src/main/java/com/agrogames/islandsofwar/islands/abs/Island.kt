package com.agrogames.islandsofwar.islands.abs

import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.map.abs.Map

class Island(val id: Int, val map: Map, val owners: Array<IUnit>, val attacker: IUnit)