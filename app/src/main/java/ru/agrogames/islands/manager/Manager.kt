package ru.agrogames.islands.manager

import ru.agrogames.islands.graphics.drawtexture.TextureDrawer

interface Manager {
    fun render(textureDrawer: TextureDrawer)
    fun onTouch(x: Float, y: Float)
    fun onMove(x: Float, y: Float)
    fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float)
}