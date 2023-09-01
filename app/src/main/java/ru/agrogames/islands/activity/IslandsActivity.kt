package ru.agrogames.islands.activity

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.agrogames.islands.common.cheats
import ru.agrogames.islands.engine.impl.Engine
import ru.agrogames.islands.factories.Factory
import ru.agrogames.islands.graphics.bitmap.BitmapProvider
import ru.agrogames.islands.graphics.gl.GLRenderer
import ru.agrogames.islands.islands.impl.JsonbinIslandProvider
import ru.agrogames.islands.manager.GameManager
import ru.agrogames.islands.manager.IndexManager
import ru.agrogames.islands.manager.MapEditorManager
import ru.agrogames.islands.manager.ShipEditorManager
import ru.agrogames.islands.map.Map
import ru.agrogames.islands.render.Renderer
import ru.agrogames.islands.sounds.SoundPlayer
import kotlin.properties.Delegates

class IslandsActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView
    private lateinit var renderer: GLRenderer
    private var currentPage: Page by Delegates.observable(Page.Start) { _, _, _ -> startView() }
    private var currentIsland: Int by Delegates.observable(0) { _, _, _ -> startView() }

    enum class Page {
        Start, Choose, Game, MapEditor, ShipEditor;

        operator fun inc() = when (this) {
            Start -> Choose
            Choose -> Game
            Game -> Game
            MapEditor -> ShipEditor
            ShipEditor -> ShipEditor
        }

        operator fun dec() = when (this) {
            Start -> Start
            Choose -> Start
            Game -> Choose
            MapEditor -> Start
            ShipEditor -> MapEditor
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
        gLView.setRenderer(renderer)
        setContentView(gLView)


        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions

        val t = this
        runBlocking {
            launch {
                withContext(Dispatchers.IO) {
                    Map.init()
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
            Page.Start -> IndexManager(this, { currentPage = Page.MapEditor }) { currentPage++ }

            Page.Choose -> GameManager(false, engine,
                Renderer(engine, false,
                    if (currentIsland == attackable.size - 1) null else { -> currentIsland++ },
                    if (currentIsland == 0) null else { -> currentIsland-- },
                    { currentPage++ }, null, false)
            )

            Page.Game -> GameManager(true, engine,
                Renderer(engine, true, null, null, null, { currentPage-- },
                    PreferenceManager.getDefaultSharedPreferences(this).cheats))

            Page.MapEditor -> MapEditorManager(this, attackable[currentIsland].id, {currentPage++}) {currentPage--}
            Page.ShipEditor -> ShipEditorManager(this) { currentPage-- }
        }
    }

    @Deprecated("Because so")
    override fun onBackPressed() {
        if (currentPage == Page.Start) {
            this.finish()
        } else {
            currentPage--
        }
    }
}