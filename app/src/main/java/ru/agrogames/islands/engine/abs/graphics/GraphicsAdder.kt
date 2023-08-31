package ru.agrogames.islands.engine.abs.graphics

interface GraphicsAdder {
    fun addGraphics(`object`: IGraphicsObject)
    val graphicsObjects: List<IGraphicsObject>
}