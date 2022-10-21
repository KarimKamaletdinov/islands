package com.agrogames.islandsofwar.graphics.impl.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Texture {
    public final float x;
    public final float y;
    public final float width;
    public final float height;
    public final float rotation;
    public final FloatBuffer vertexBuffer;
    public final float[] rotationMatrix;
    private final Bitmap textureBitmap;

    public Texture(float x, float y, Bitmap bitmap, float width, float height, float rotation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        textureBitmap = bitmap;

        float right = x + width / 2.0f;
        float left = x - width / 2.0f;

        float top = y + height / 2.0f;
        float bottom = y - height / 2.0f;

        float[] triangleCoords = {
                left, top,
                left, bottom,
                right, bottom,
                right, top,
        };

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        rotationMatrix = new float[]{
                (float)Math.cos(rotation), -(float)Math.sin(rotation),
                (float)Math.sin(rotation), (float)Math.cos(rotation)
        };
    }

    public void Render(int mProgram, float[] vPMatrix) {
        GLES20.glUseProgram(mProgram);
        int textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        int[] textureUnit = { 1 };
        GLES20.glGenTextures(textureUnit.length, textureUnit, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glUniform1i(textureUniformHandle, 0);

        int vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0);

        int rMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uRotationMatrix");
        GLES20.glUniformMatrix2fv(rMatrixHandle, 1, false, rotationMatrix, 0);

        int cPHandle = GLES20.glGetUniformLocation(mProgram, "uCenterPosition");
        GLES20.glUniform2f(cPHandle, x, y);

        int texturePositionHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
        GLES20.glVertexAttribPointer(texturePositionHandle,
                GlConstants.coordsPerVertex,
                GLES20.GL_FLOAT,
                false,
                GlConstants.coordsPerVertex * 4,
                GlConstants.textureBuffer);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glVertexAttribPointer(positionHandle, GlConstants.coordsPerVertex,
                GLES20.GL_FLOAT, false,
                GlConstants.coordsPerVertex * 4, vertexBuffer);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(texturePositionHandle);

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                GlConstants.drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,
                GlConstants.drawOrderBuffer
        );

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texturePositionHandle);
        GLES20.glDeleteTextures(textureUnit.length, textureUnit, 0);
    }
}
