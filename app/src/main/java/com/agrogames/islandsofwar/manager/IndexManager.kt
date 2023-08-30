package com.agrogames.islandsofwar.manager

import android.content.Context
import androidx.preference.PreferenceManager
import com.agrogames.islandsofwar.common.sound
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.graphics.impl.drawtexture.TextureDrawer
import com.agrogames.islandsofwar.sounds.SoundPlayer
import com.agrogames.islandsofwar.ui.impl.Element
import com.agrogames.islandsofwar.ui.impl.ElementType
import com.agrogames.islandsofwar.ui.impl.UI

class IndexManager(context: Context, play: () -> Unit) : Manager {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val ui = UI()

    init {
        ui.createElement(Element(ElementType.Button, 7.5f, 5f, 3f, 3f, "ui/start_game", {
            play()
        }))

        ui.createElement(Element(
            ElementType.Button, 13f, 8f, 1f, 1f,
            soundTexture(), {
                preferences.sound = !preferences.sound
                this.texture = soundTexture()
                SoundPlayer.enabled = preferences.sound
            }))
    }

    private var touch: Point? = null

    override fun render(textureDrawer: TextureDrawer) {
        ui.render(textureDrawer, touch)
        touch = null
    }

    override fun onTouch(x: Float, y: Float) {
        touch = Point(x, y)
    }

    override fun onMove(x: Float, y: Float) {}

    override fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float) {}

    private fun soundTexture() = if(preferences.sound) "ui/sound_enabled" else "ui/sound_disabled"
}