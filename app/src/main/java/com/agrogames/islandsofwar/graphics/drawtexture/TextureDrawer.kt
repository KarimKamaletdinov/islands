package com.agrogames.islandsofwar.graphics.drawtexture

import android.util.Pair
import com.agrogames.islandsofwar.graphics.bitmap.BitmapProvider
import com.agrogames.islandsofwar.graphics.gl.Texture

class TextureDrawer {
    private val textures: MutableList<Texture> = ArrayList()
    private var currentX = 0f
    private var currentY = 0f
    private var currentZoom = 1f
    fun drawTexture(x: Float, y: Float, bitmap: String, width: Float, height: Float, rotation: Float) {
        textures.add(Texture(x * currentZoom + currentX, y * currentZoom + currentY, BitmapProvider.load(bitmap).id, width * currentZoom, height * currentZoom, rotation))
    }

    fun drawTexture(x: Float, y: Float, bitmap: String, rotation: Float): Pair<Float, Float> {
        val b = BitmapProvider.load(bitmap)
        val width = b.width / 50f
        val height = b.height / 50f
        textures.add(Texture(x * currentZoom + currentX, y * currentZoom + currentY, b.id, width * currentZoom, height * currentZoom, rotation))
        return Pair(width, height)
    }

    fun getSize(bitmap: String): Pair<Float, Float> {
        val b = BitmapProvider.load(bitmap)
        val width = b.width / 50f
        val height = b.height / 50f
        return Pair(width, height)
    }

    fun translate(x: Float, y: Float) {
        currentX += x
        currentY += y
    }

    fun scale(currentZoom: Float) {
        this.currentZoom *= currentZoom
    }

    fun GetTextures(): Array<Texture> {
        val result = textures.toTypedArray()
        textures.clear()
        return result
    }
}