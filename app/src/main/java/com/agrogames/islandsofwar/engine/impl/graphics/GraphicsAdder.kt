package com.agrogames.islandsofwar.engine.impl.graphics

import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder
import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject
import com.agrogames.islandsofwar.sounds.SoundPlayer

class GraphicsAdder() : GraphicsAdder {
    override val graphicsObjects: MutableList<IGraphicsObject> = ArrayList()
    override fun addGraphics(`object`: IGraphicsObject) {
        graphicsObjects.add(`object`)
        if (`object`.spawnSound != null) {
            SoundPlayer.playSound(`object`.spawnSound)
        }
    }
}