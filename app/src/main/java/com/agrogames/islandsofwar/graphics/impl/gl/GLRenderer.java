package com.agrogames.islandsofwar.graphics.impl.gl;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.graphics.impl.bitmap.BitmapProvider;
import com.agrogames.islandsofwar.graphics.impl.drawtexture.TextureDrawer;

import java.io.IOException;
import java.util.Scanner;

public class GLRenderer implements GLSurfaceView.Renderer {

    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private int mProgram;


    private String vertexShaderCode;
    private String fragmentShaderCode;

    @androidx.annotation.NonNull
    private final Context context;
    private RenderManager manager;
    private TextureDrawer textureDrawer;
    private int width;
    private int height;

    public GLRenderer(Context context, RenderManager manager) {
        this.context = context;
        this.manager = manager;
        try {
            vertexShaderCode = new Scanner(context.getAssets().open("shaders/vertex.vert")).useDelimiter("\\A").next();
            fragmentShaderCode = new Scanner(context.getAssets().open("shaders/fragment.frag")).useDelimiter("\\A").next();
        }
        catch (IOException e){
            vertexShaderCode = "CAN'T LOAD VERTEX SHADER";
            fragmentShaderCode = "CAN'T LOAD FRAGMENT SHADER";
        }
    }


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        GLES20.glClearColor(95 / 255f, 177 / 255f, 222 / 255f, 1.0f);
        textureDrawer = new TextureDrawer(new BitmapProvider(context, mProgram));
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        manager.render(textureDrawer);
        Texture[] textures = textureDrawer.GetTextures();
        for (Texture texture: textures) {
            texture.render(mProgram, vPMatrix);
        }

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        //String log = GLES20.glGetShaderInfoLog(shader);
        return shader;
    }

    public void onTouch(float x, float y){
        manager.onTouch(convertX(x), convertY(y));
    }

    public void onMove(float x, float y){
        manager.onMove(convertX(x), convertY(y));
    }

    public void onZoom(float x1, float y1, float x2, float y2) {
        manager.onZoom(convertX(x1), convertY(y1), convertX(x2), convertY(y2));
    }

    public void setManager(RenderManager m){
        manager = m;
    }

    private float convertX(float x){
        return (x - (width - height * 1.5f) / 2) / height * 10f;
    }

    private float convertY(float y){
        return 10f - (y / height * 10f);
    }
}
