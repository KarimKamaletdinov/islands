package ru.agrogames.islands.render

import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.map.MapParams

object MapScroller {
    private var currentX = 0f
    private var currentY = 0f
    private var currentZoom = 1f
    fun start(drawer: TextureDrawer) {
        drawer.translate(currentX, currentY)
        drawer.scale(currentZoom)
    }

    fun finish(drawer: TextureDrawer) {
        drawer.translate(-currentX, -currentY)
        drawer.scale(1 / currentZoom)
    }

    fun reset() {
        currentX = 0f
        currentY = 0f
        currentZoom = 1f
    }

    fun convert(point: Point): Point {
        return Point(point.x / currentZoom - currentX, point.y / currentZoom - currentY)
    }

    fun scroll(x: Float, y: Float) {
        currentX += x
        currentY += y
        if (currentX > 0) currentX = 0f
        if (currentY > 0) currentY = 0f
        if (currentX < -MapParams.width + 15) currentX = (-MapParams.width + 15).toFloat()
        if (currentY < -MapParams.height + 8) currentY = (-MapParams.height + 8).toFloat()
    }

    fun zoom(zoom1: Point, zoom2: Point, previousZoom1: Point, previousZoom2: Point) {
        var diff = M.dist(zoom1, zoom2) - M.dist(previousZoom1, previousZoom2)
        if (java.lang.Float.isNaN(diff)) diff = 0f
        val oldZoom = currentZoom
        currentZoom += diff / 5f
        if (currentZoom > 1) currentZoom = 1f
        if (currentZoom < 0.5f) currentZoom = 0.5f
        val middle = M.middle(zoom1, zoom2)
        currentX = -(-currentX + middle!!.x / oldZoom - middle.x / currentZoom)
        currentY = -(-currentY + middle.y / oldZoom - middle.y / currentZoom)
        scroll(0f, 0f)
    }
}