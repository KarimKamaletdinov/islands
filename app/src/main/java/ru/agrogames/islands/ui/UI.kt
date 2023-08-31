package ru.agrogames.islands.ui

import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class UI {
    private val elements: MutableList<Element> = ArrayList()
    private var fps: Int = 0
    private var frames = 0
    private var lastCheck: LocalTime? = null

    fun createElement(element: Element): Element {
        elements.add(element)
        return element
    }

    fun deleteElement(element: Element) {
        elements.remove(element)
    }

    fun render(drawer: TextureDrawer, touch: Point?): Boolean {
        val now = LocalTime.now()
        frames++
        if(lastCheck == null) lastCheck = now
        val delta = lastCheck!!.until(now, ChronoUnit.MILLIS)
        if(delta.toInt() >= 1000) {
            fps = frames
            frames = 0
            lastCheck = now
        }
        var x = 0f
        for(c in fps.toString()){
            drawer.drawTexture(15f + x, 9.5f, "ui/n$c", 0.25f, 0.5f, 0f)
            x += 0.3f
        }
        for (element in elements) {
            if (!element.visible) continue
            if (element.renderInBorders) {
                drawer.drawTexture(element.x, element.y, element.texture,
                        element.width, element.height, 0f)
            } else {
                if (element.width == -1f || element.height == -1f) {
                    val size = drawer.drawTexture(element.x, element.y, element.texture, 0f)
                    element.width = size.first
                    element.height = size.second
                } else {
                    val size = drawer.getSize(element.texture)
                    if (size.first > element.width && size.second > element.height) {
                        element.renderInBorders = true
                        drawer.drawTexture(element.x, element.y, element.texture,
                                element.width, element.height, 0f)
                    } else {
                        drawer.drawTexture(element.x, element.y, element.texture, 0f)
                        element.width = size.first
                        element.height = size.second
                    }
                }
            }
            if (touch != null) {
                if (element.x + element.width / 2 > touch.x && element.x - element.width / 2 < touch.x
                    && element.y + element.height / 2 > touch.y && element.y - element.height / 2 < touch.y) {
                    if (element.onClick != null) {
                        element.onClick?.let { it(element) }
                        return true
                    }
                }
            }
        }
        return false
    }
}