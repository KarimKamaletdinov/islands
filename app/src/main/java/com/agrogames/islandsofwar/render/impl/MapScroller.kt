package com.agrogames.islandsofwar.render.impl

import com.agrogames.islandsofwar.common.M
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.graphics.drawtexture.TextureDrawer
import com.agrogames.islandsofwar.map.abs.MapParams

object MapScroller {
    private var currentX = 0f
    private var currentY = 0f
    private var currentZoom = 1f
    fun start(drawer: TextureDrawer?) {
        drawer!!.translate(currentX, currentY)
        drawer.scale(currentZoom)
    }

    fun finish(drawer: TextureDrawer?) {
        drawer!!.translate(-currentX, -currentY)
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
        if (currentX < -MapParams.Width + 15) currentX = (-MapParams.Width + 15).toFloat()
        if (currentY < -MapParams.Height + 8) currentY = (-MapParams.Height + 8).toFloat()
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