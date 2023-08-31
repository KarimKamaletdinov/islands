package ru.agrogames.islands.render

import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer

class ObjectRenderer(private val drawer: TextureDrawer) {
    fun render(`object`: RenderableObject): Float {
        return simpleRender(`object`, TextureMapper.join(`object`.texture))
    }

    fun render(`object`: RenderableObject, state: String): Float {
        return simpleRender(`object`, TextureMapper.join(`object`.texture, state))
    }

    private fun simpleRender(`object`: RenderableObject, texture: String): Float {
        val location = `object`.location
        val size = drawer.drawTexture(location.x, location.y, texture, `object`.rotation)
        return if (size.first!! > size.second!!) size.first!! else size.second!!
    }
}