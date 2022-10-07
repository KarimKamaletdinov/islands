package com.agrogames.islandsofwar.graphics.gl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlConstants {
    public static final int coordsPerVertex = 2;

    public static final float[] textureCoords = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    public static final Buffer textureBuffer =  ByteBuffer.allocateDirect(textureCoords.length * 4).
            order(ByteOrder.nativeOrder())
            .asFloatBuffer().
            put(textureCoords).
            position(0);

    public static final short[] drawOrder = {0, 1, 2, 0, 2, 3};
    public static final Buffer drawOrderBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(drawOrder)
            .position(0);
}
