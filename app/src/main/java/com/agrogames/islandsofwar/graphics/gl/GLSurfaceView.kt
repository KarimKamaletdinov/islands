package com.agrogames.islandsofwar.graphics.gl

/*
@SuppressLint("ViewConstructor")
class GLSurfaceView(context: Context, manager: RenderManager) : GLSurfaceView(context) {
    private val renderer: GLRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = GLRenderer(context, manager)
        setOnTouchListener { v: View, e: MotionEvent ->
            v.performClick()
            if (e.action == MotionEvent.ACTION_UP) {
                renderer.onTouch(e.x, e.y)
            } else if (e.action == MotionEvent.ACTION_MOVE) {
                if (e.pointerCount == 1) {
                    renderer.onMove(e.x, e.y)
                } else {
                    renderer.onZoom(e.getX(0), e.getY(0), e.getX(1), e.getY(1))
                }
            }
            true
        }
        setRenderer(renderer)
    }

    fun setManager(m: RenderManager) {
        renderer.setManager(m)
    }
}*/