package com.agrogames.islandsofwar.graphics.gl

import java.nio.ByteBuffer
import java.nio.ByteOrder

object GlConstants {
    const val coordsPerVertex = 2
    val textureCoords = floatArrayOf(
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f)
    val textureBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().put(textureCoords).position(0)
    val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
    val drawOrderBuffer = ByteBuffer.allocateDirect(drawOrder.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(drawOrder)
            .position(0)
}