package com.agrogames.islandsofwar.manager

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.impl.Engine
import com.agrogames.islandsofwar.graphics.drawtexture.TextureDrawer
import com.agrogames.islandsofwar.render.impl.Renderer
import com.agrogames.islandsofwar.sounds.SoundPlayer
import kotlinx.coroutines.DisposableHandle
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Timer
import java.util.TimerTask

class GameManager(private val runEngine: Boolean, private val engine: Engine,
                  private val renderer: Renderer) : DisposableHandle, Manager {
    private var previous: LocalTime? = null
    private var touch: Point? = null
    private var move: Point? = null
    private var previousMove: Point? = null
    private var zoom1: Point? = null
    private var previousZoom1: Point? = null
    private var zoom2: Point? = null
    private var previousZoom2: Point? = null
    private val timer = Timer()

    init {
        if(runEngine){
            timer.schedule(object : TimerTask() {
                override fun run() {
                    SoundPlayer.playSound("music")
                }
            }, 3000, 10000)
        }
    }

    override fun render(textureDrawer: TextureDrawer) {
        if (runEngine) {
            val now = LocalTime.now()
            if (previous == null) {
                previous = now
            }
            val deltaTime = previous!!.until(now, ChronoUnit.MICROS).toFloat() / 1000000f
            previous = now
            engine.update(deltaTime)
        }
        this.renderer.render(textureDrawer, SoundPlayer, touch, move, previousMove, zoom1, zoom2, previousZoom1, previousZoom2)
        touch = null
        previousMove = move
        previousZoom1 = zoom1
        previousZoom2 = zoom2
        move = null
        zoom1 = null
        zoom2 = null
    }

    override fun onTouch(x: Float, y: Float) {
        touch = Point(x, y)
    }

    override fun onMove(x: Float, y: Float) {
        move = Point(x, y)
    }

    override fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float) {
        zoom1 = Point(x1, y1)
        zoom2 = Point(x2, y2)
    }

    override fun dispose() {
        timer.cancel()
    }
}

