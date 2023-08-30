package com.agrogames.islandsofwar.engine.abs.map

interface Map {
    fun isTaken(x: Int, y: Int): Boolean
}