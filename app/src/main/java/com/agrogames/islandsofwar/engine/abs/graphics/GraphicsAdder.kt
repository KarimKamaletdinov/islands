package com.agrogames.islandsofwar.engine.abs.graphics

interface GraphicsAdder {
    fun addGraphics(`object`: IGraphicsObject)
    val graphicsObjects: List<IGraphicsObject>
}