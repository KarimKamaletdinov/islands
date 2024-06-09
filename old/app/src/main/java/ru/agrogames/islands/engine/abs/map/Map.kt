package ru.agrogames.islands.engine.abs.map

interface Map {
    fun isTaken(x: Int, y: Int): Boolean
}