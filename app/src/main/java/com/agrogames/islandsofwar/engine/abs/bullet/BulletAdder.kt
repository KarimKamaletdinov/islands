package com.agrogames.islandsofwar.engine.abs.bullet

interface BulletAdder {
    val bullets: List<IBullet>
    fun addBullet(bullet: IBullet)
}