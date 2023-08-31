package ru.agrogames.islands.manager

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ru.agrogames.islands.common.sound
import ru.agrogames.islands.common.toStringList
import ru.agrogames.islands.common.units
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.islands.impl.JsonbinIslandProvider
import ru.agrogames.islands.ui.Element
import ru.agrogames.islands.ui.ElementType
import ru.agrogames.islands.ui.UI
import java.util.Scanner

class ShipEditorManager(context: Context, private val back: () -> Unit) : Manager {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val ui = UI()
    private val spawnUnits = JSONObject(
        Scanner(context.assets.open("units.json")).useDelimiter("\\A").next()
    ).getJSONArray("units").let {
        (0 until it.length()).map { i -> it.getJSONObject(i) }
    }.filter { it.getString("type") == "land" }
    private val count = preferences.units ?:
        mutableMapOf(*spawnUnits.map { Pair(it.getString("name"), 0) }.toTypedArray())

    init {
        for (unit in spawnUnits.withIndex()) {
            ui.createElement(Element(ElementType.Button, 1f, 9f - unit.index * 1.5f, 1f, 1f, "ui/minus", {
                if(count[unit.value.getString("name")] != 0) {
                    count[unit.value.getString("name")] = count[unit.value.getString("name")]!! - 1
                    preferences.units = count
                }
            }))
            ui.createElement(Element(ElementType.Button, 3f, 9f - unit.index * 1.5f, 1f, 1f, "ui/plus",{
                count[unit.value.getString("name")] = count[unit.value.getString("name")]!! + 1
                preferences.units = count
            }))
            ui.createElement(
                Element(
                    ElementType.Button,
                    2f,
                    9f - unit.index * 1.5f,
                    1f,
                    1f,
                    "units/" + (if (unit.value.has("texture")) unit.value.getString("texture")
                    else unit.value.getString("name")) + "/normal",
                    renderInBorders = false
                )
            )
            if (unit.value.has("weapons")) {
                for (weapon in unit.value.getJSONArray("weapons").toStringList()) {
                    ui.createElement(
                        Element(
                            ElementType.Button, 2f, 9f - unit.index * 1.5f, 1f, 1f,
                            "weapons/$weapon", renderInBorders = false
                        )
                    )
                }
            }
        }
        ui.createElement(
            Element(ElementType.Button, 1f, 1f, 4f, 1f, "ui/back_button",
                {
                    back()
                })
        )
        ui.createElement(
            Element(
                ElementType.Button, 9f, 1f, 5f, 1f,
                "ui/release_button", {
                    runBlocking {
                        launch {
                            withContext(Dispatchers.IO) {
                                JsonbinIslandProvider(context).saveIsland()
                                back()
                                back()
                            }
                        }
                    }
                })
        )
    }

    private var touch: Point? = null

    override fun render(textureDrawer: TextureDrawer) {
        ui.render(textureDrawer, touch)
        var y = 0f
        for ((_, v) in count){
            var x = 0f
            for(c in v.toString()){
                textureDrawer.drawTexture(4.5f + x, 9 - y, "ui/n$c", 0f)
                x += 0.75f
            }
            y += 1.5f
        }
        touch = null
    }

    override fun onTouch(x: Float, y: Float) {
        touch = Point(x, y)
    }

    override fun onMove(x: Float, y: Float) {}

    override fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float) {}

    private fun soundTexture() = if (preferences.sound) "ui/sound_enabled" else "ui/sound_disabled"
}