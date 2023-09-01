package ru.agrogames.islands.manager

import android.content.Context
import androidx.preference.PreferenceManager
import ru.agrogames.islands.common.cheats
import ru.agrogames.islands.common.sound
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.sounds.SoundPlayer
import ru.agrogames.islands.ui.Element
import ru.agrogames.islands.ui.ElementType
import ru.agrogames.islands.ui.UI

class IndexManager(context: Context, editor: () -> Unit, play: () -> Unit) : Manager {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val ui = UI()
    private val cheatsButtonTexture
        get() = if(preferences.cheats) "other/red" else "other/transparent"

    init {
        ui.createElement(Element(ElementType.Button, 7.5f, 5f, 3f, 3f, "ui/start_game", {
            play()
        }))

        ui.createElement(Element(ElementType.Button, 15f, 9.5f, 1f, 1f, cheatsButtonTexture, {
            preferences.cheats = !preferences.cheats
            this.texture = cheatsButtonTexture
        }))

        ui.createElement(
            Element(
            ElementType.Button, 13f, 8f, 1f, 1f,
            soundTexture(), {
                preferences.sound = !preferences.sound
                this.texture = soundTexture()
                SoundPlayer.enabled = preferences.sound
            })
        )

        ui.createElement(
            Element(
            ElementType.Button, 1f, 8f, 7f, 1f,
            "ui/editor_button", {editor()})
        )
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