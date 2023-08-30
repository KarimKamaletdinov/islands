package com.agrogames.islandsofwar.ui.impl

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.graphics.impl.drawtexture.TextureDrawer

class UI {
    private val elements: MutableList<Element> = ArrayList()
    fun createElement(element: Element): Element {
        elements.add(element)
        return element
    }

    fun deleteElement(element: Element) {
        elements.remove(element)
    }

    fun render(drawer: TextureDrawer, touch: Point?): Boolean {
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