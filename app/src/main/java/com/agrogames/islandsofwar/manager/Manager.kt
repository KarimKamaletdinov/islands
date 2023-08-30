package com.agrogames.islandsofwar.manager

import com.agrogames.islandsofwar.graphics.impl.drawtexture.TextureDrawer

interface Manager {
    fun render(textureDrawer: TextureDrawer)
    fun onTouch(x: Float, y: Float)
    fun onMove(x: Float, y: Float)
    fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float)
}