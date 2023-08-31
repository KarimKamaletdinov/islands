package ru.agrogames.islands.engine.abs.bullet

interface BulletAdder {
    val bullets: List<IBullet>
    fun addBullet(bullet: IBullet)
}