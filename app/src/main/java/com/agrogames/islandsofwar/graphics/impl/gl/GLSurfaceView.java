package com.agrogames.islandsofwar.graphics.impl.gl;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.agrogames.islandsofwar.graphics.abs.RenderManager;

public class GLSurfaceView extends android.opengl.GLSurfaceView {

    public GLSurfaceView(Context context, RenderManager manager){
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        GLRenderer renderer = new GLRenderer(context, manager);
        setOnTouchListener((v, e) -> {
            v.performClick();
            if(e.getAction() == MotionEvent.ACTION_UP){
                renderer.onTouch(e.getX(), e.getY());
            }
            return true;
        });
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}

