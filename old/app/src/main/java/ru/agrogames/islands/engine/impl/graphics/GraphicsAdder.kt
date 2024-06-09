package ru.agrogames.islands.engine.impl.graphics

import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.graphics.IGraphicsObject
import ru.agrogames.islands.sounds.SoundPlayer

class GraphicsAdder() : GraphicsAdder {
    override val graphicsObjects: MutableList<IGraphicsObject> = ArrayList()
    override fun addGraphics(`object`: IGraphicsObject) {
        graphicsObjects.add(`object`)
        if (`object`.spawnSound != null) {
            SoundPlayer.playSound(`object`.spawnSound)
        }
    }
}