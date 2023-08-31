package com.agrogames.islandsofwar.graphics.gl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Texture(val x: Float, val y: Float, private val textureBitmap: Int, val width: Float, val height: Float, val rotation: Float) {
    val vertexBuffer: FloatBuffer
    val rotationMatrix: FloatArray

    init {
        val right = x + width / 2.0f
        val left = x - width / 2.0f
        val top = y + height / 2.0f
        val bottom = y - height / 2.0f
        val triangleCoords = floatArrayOf(
                left, top,
                left, bottom,
                right, bottom,
                right, top)
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)
        rotationMatrix = floatArrayOf(Math.cos(rotation.toDouble()).toFloat(), -Math.sin(rotation.toDouble()).toFloat(), Math.sin(rotation.toDouble()).toFloat(), Math.cos(rotation.toDouble()).toFloat())
    }

    fun render(mProgram: Int, vPMatrix: FloatArray?) {
        GLES20.glUseProgram(mProgram)

        val textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture")
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureBitmap)
        GLES20.glUniform1i(textureUniformHandle, 0)
        val vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)
        val rMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uRotationMatrix")
        GLES20.glUniformMatrix2fv(rMatrixHandle, 1, false, rotationMatrix, 0)
        val cPHandle = GLES20.glGetUniformLocation(mProgram, "uCenterPosition")
        GLES20.glUniform2f(cPHandle, x, y)
        val texturePositionHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate")
        GLES20.glVertexAttribPointer(texturePositionHandle,
                GlConstants.coordsPerVertex,
                GLES20.GL_FLOAT,
                false,
                GlConstants.coordsPerVertex * 4,
                GlConstants.textureBuffer)
        val positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        GLES20.glVertexAttribPointer(positionHandle, GlConstants.coordsPerVertex,
                GLES20.GL_FLOAT, false,
                GlConstants.coordsPerVertex * 4, vertexBuffer)
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(texturePositionHandle)
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                GlConstants.drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT,
                GlConstants.drawOrderBuffer
        )
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texturePositionHandle)
        //GLES20.glDeleteTextures(textureUnit.length, textureUnit, 0);
    }
}