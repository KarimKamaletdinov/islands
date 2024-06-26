package ru.agrogames.islands.islands.abs

interface IslandProvider {
    val my: Array<Island>
    val attackable: Array<Island>
    fun getMyById(id: Int): Island
    fun getAttackableById(id: Int): Island
}