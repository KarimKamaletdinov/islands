package ru.agrogames.islands.graphics.gl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import ru.agrogames.islands.common.mProgram
import ru.agrogames.islands.graphics.bitmap.BitmapProvider
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.manager.Manager
import java.io.IOException
import java.util.Scanner
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer(context: Context, var manager: Manager?) : GLSurfaceView.Renderer {
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private var vertexShaderCode: String? = null
    private var fragmentShaderCode: String? = null
    private lateinit var textureDrawer: TextureDrawer
    private var width = 0
    private var height = 0
    private var loaded = false

    init {
        try {
            vertexShaderCode = Scanner(context.assets.open("shaders/vertex.vert")).useDelimiter("\\A").next()
            fragmentShaderCode = Scanner(context.assets.open("shaders/fragment.frag")).useDelimiter("\\A").next()
        } catch (e: IOException) {
            vertexShaderCode = "CAN'T LOAD VERTEX SHADER"
            fragmentShaderCode = "CAN'T LOAD FRAGMENT SHADER"
        }
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        BitmapProvider.initProgram()
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
            vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode)
        GLES20.glAttachShader(mProgram, vertexShader)
        GLES20.glAttachShader(mProgram, fragmentShader)
        GLES20.glLinkProgram(mProgram)
        GLES20.glClearColor(95 / 255f, 177 / 255f, 222 / 255f, 1.0f)
        textureDrawer = TextureDrawer()
        loaded = true
    }

    override fun onDrawFrame(unused: GL10) {
        if (!loaded) return
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        manager?.render(textureDrawer)
        val textures = textureDrawer.GetTextures()
        for (texture in textures) {
            texture.render(mProgram, vPMatrix)
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    fun onTouch(x: Float, y: Float) {
        manager?.onTouch(convertX(x), convertY(y))
    }

    fun onMove(x: Float, y: Float) {
        manager?.onMove(convertX(x), convertY(y))
    }

    fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float) {
        manager?.onZoom(convertX(x1), convertY(y1), convertX(x2), convertY(y2))
    }

    private fun convertX(x: Float): Float {
        return (x - (width - height * 1.5f) / 2) / height * 10f
    }

    private fun convertY(y: Float): Float {
        return 10f - y / height * 10f
    }

    companion object {
        fun loadShader(type: Int, shaderCode: String?): Int {
            val shader = GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            //String log = GLES20.glGetShaderInfoLog(shader);
            return shader
        }
    }
}