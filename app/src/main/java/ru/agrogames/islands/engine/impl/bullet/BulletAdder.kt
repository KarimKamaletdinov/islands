package ru.agrogames.islands.engine.impl.bullet

import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.bullet.IBullet
import ru.agrogames.islands.sounds.SoundPlayer

class BulletAdder() : BulletAdder {
    override val bullets: MutableList<IBullet> = ArrayList()
    override fun addBullet(bullet: IBullet) {
        bullets.add(bullet)
        if (bullet.creationSound != null) {
            SoundPlayer.playSound(bullet.creationSound)
        }
    }
}