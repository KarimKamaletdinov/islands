package com.agrogames.islandsofwar.engine.impl.bullet

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet
import com.agrogames.islandsofwar.sounds.SoundPlayer

class BulletAdder() : BulletAdder {
    override val bullets: MutableList<IBullet> = ArrayList()
    override fun addBullet(bullet: IBullet) {
        bullets.add(bullet)
        if (bullet.creationSound != null) {
            SoundPlayer.playSound(bullet.creationSound)
        }
    }
}