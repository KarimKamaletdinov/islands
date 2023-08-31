package com.agrogames.islandsofwar.activity

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.agrogames.islandsofwar.engine.impl.Engine
import com.agrogames.islandsofwar.factories.Factory
import com.agrogames.islandsofwar.graphics.bitmap.BitmapProvider
import com.agrogames.islandsofwar.graphics.gl.GLRenderer
import com.agrogames.islandsofwar.islands.impl.JsonbinIslandProvider
import com.agrogames.islandsofwar.manager.GameManager
import com.agrogames.islandsofwar.manager.IndexManager
import com.agrogames.islandsofwar.render.impl.Renderer
import com.agrogames.islandsofwar.sounds.SoundPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class SelectIslandActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView
    private lateinit var renderer: GLRenderer
    private var currentPage: Page by Delegates.observable(Page.Start) { _, _, _ -> startView() }
    private var currentIsland: Int by Delegates.observable(0) { _, _, _ -> startView() }

    enum class Page {
        Start, Choose, Game;
        operator fun dec() = when(this){
            Start -> Start
            Choose -> Start
            Game -> Choose
        }
        operator fun inc() = when(this){
            Start -> Choose
            Choose -> Game
            Game -> Game
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Factory.load(this)
        renderer = GLRenderer(this, null)
        gLView = GLSurfaceView(this)
        gLView.setEGLContextClientVersion(2)
        gLView.setOnTouchListener { v: View, e ->
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
        //gLView.setRenderer(renderer)
        //setContentView(gLView)


        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions

        val t = this
        runBlocking {
            launch {
                withContext(Dispatchers.IO) {
                    Factory.load(t)
                    BitmapProvider.initCache(t)
                    SoundPlayer.init(t)
                    startView()
                }
            }
        }
    }

    private fun startView() {
        val attackable = JsonbinIslandProvider(this).attackable
        val engine = Engine(attackable[currentIsland])
        Log.i("IOW", "aa")
        this.renderer.manager = when (currentPage) {
            Page.Start -> IndexManager(this) { currentPage++ }

            Page.Choose -> GameManager(false, engine, Renderer(
                engine, false,
                if (currentIsland == attackable.size - 1) null else { -> currentIsland++ },
                if (currentIsland == 0) null else { -> currentIsland-- },
                { currentPage++ },
                null,
                true
            ))

            Page.Game -> GameManager(true, engine, Renderer(engine,  true, null, null, null, {
                currentPage--
            }, true))
        }
    }

    @Deprecated("Because so")
    override fun onBackPressed() {
        if(currentPage == Page.Start){
            this.finish()
        } else {
            currentPage--
        }
    }
}