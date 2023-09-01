package ru.agrogames.islands.manager

import android.content.Context
import androidx.preference.PreferenceManager
import org.json.JSONObject
import ru.agrogames.islands.common.island
import ru.agrogames.islands.common.toStringList
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.map.Map
import ru.agrogames.islands.map.MapParams
import ru.agrogames.islands.render.MapScroller
import ru.agrogames.islands.ui.Element
import ru.agrogames.islands.ui.ElementType
import ru.agrogames.islands.ui.UI
import java.util.LinkedList
import java.util.Scanner
import kotlin.properties.Delegates

class MapEditorManager(context: Context, private val islandId: Int, private val viewShip: () -> Unit,
                       private val back: () -> Unit) : Manager {
    private val ui = UI()
    private val spawnUnits = JSONObject(Scanner(context.assets.open("units.json")).useDelimiter("\\A").next()).getJSONArray("units").let {
        (0 until it.length()).map { i -> it.getJSONObject(i) }
    }
    private var selected: String? by Delegates.observable(null) { _, _, _ -> renderSpawnUnits() }
    private val spawnUnitsUi = LinkedList<Element>()
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val units = preferences.island
    private val map = Map.fromJsonBin("map$islandId.txt")
    init {
        ui.createElement(
            Element(
                ElementType.Button, 1f, 1f, 4f, 1f,
                "ui/back_button", {back()})
        )
        ui.createElement(
            Element(
            ElementType.Button, 9f, 1f, 6f, 1f,
            "ui/to_ship_button", {viewShip()})
        )
        renderSpawnUnits()
    }

    private fun renderSpawnUnits() {
        for (unitUi in spawnUnitsUi){
            ui.deleteElement(unitUi)
        }
        spawnUnitsUi.clear()
        for (unit in spawnUnits.withIndex()) {
            if (unit.value.getString("type") == "land" || unit.value.getString("type") == "building") {
                spawnUnitsUi.add(
                    ui.createElement(
                        Element(ElementType.Button, 14f, 9f - unit.index.toFloat(), 1.2f, 1.2f,
                            "ui/button_background", {
                                selected =
                                    if(selected == unit.value.getString("name") ) null
                                    else unit.value.getString("name")
                            })
                    )
                )
                spawnUnitsUi.add(
                    ui.createElement(
                        Element(
                            ElementType.Button,
                            14f,
                            9f - unit.index.toFloat(),
                            1f,
                            1f,
                            "units/" + (if (unit.value.has("texture")) unit.value.getString("texture")
                            else unit.value.getString("name")) +
                                    if (selected == unit.value.getString("name")) "/selected" else "/normal",
                            renderInBorders = false
                        )
                    )
                )
                if (unit.value.has("weapons")) {
                    for (weapon in unit.value.getJSONArray("weapons").toStringList()) {
                        spawnUnitsUi.add(
                            ui.createElement(
                                Element(
                                    ElementType.Button, 14f, 9f - unit.index.toFloat(), 1f, 1f,
                                    "weapons/$weapon", renderInBorders = false
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private var touchPoint: Point? = null
    private var movePoint: Point? = null
    private var zoom1: Point? = null
    private var zoom2: Point? = null
    private var previousMovePoint: Point? = null
    private var previousZoom1: Point? = null
    private var previousZoom2: Point? = null

    override fun render(textureDrawer: TextureDrawer) {
        MapScroller.start(textureDrawer)
        textureDrawer.drawTexture(15f, 10f, "imagekit/map$islandId", 30f, 20f, 0f)
        for (x in units.withIndex()){
            for(y in x.value.withIndex()){
                if(y.value != null){
                    val unit = spawnUnits.first { it.getString("name") == y.value }
                    textureDrawer.drawTexture(x.index.toFloat(), y.index.toFloat(),
                        "units/" +
                                (if (unit.has("texture")) unit.getString("texture")
                                else unit.getString("name"))
                                + "/normal", 0f)
                    for (weapon in unit.getJSONArray("weapons").toStringList()) {
                        textureDrawer.drawTexture(x.index.toFloat(), y.index.toFloat(),
                            "weapons/$weapon", 0f)
                    }
                }
            }
        }
        MapScroller.finish(textureDrawer)
        if (touchPoint == null) ui.render(textureDrawer, null)
        else if (!ui.render(textureDrawer, touchPoint)){
            val p = MapScroller.convert(touchPoint!!)
            val c = Cell(p)
            if(map.map.none { c in it.territory } && c.x >= 0 && c.x < MapParams.width
                    && c.y >= 0 && c.y < MapParams.height) {
                units[c.x][c.y] = selected
                preferences.island = units
            }
        }
        if (movePoint != null && previousMovePoint != null)
            MapScroller.scroll(movePoint!!.x - previousMovePoint!!.x, movePoint!!.y - previousMovePoint!!.y)
        if (zoom1 != null && zoom2 != null && previousZoom1 != null && previousZoom2 != null)
            MapScroller.zoom(zoom1!!, zoom2!!, previousZoom1!!, previousZoom2!!)
        previousMovePoint = movePoint
        previousZoom1 = zoom1
        previousZoom2 = zoom2
        touchPoint = null
        movePoint = null
        zoom1 = null
        zoom2 = null
    }

    override fun onTouch(x: Float, y: Float) {
        touchPoint = Point(x, y)
    }

    override fun onMove(x: Float, y: Float) {
        movePoint = Point(x, y)
    }

    override fun onZoom(x1: Float, y1: Float, x2: Float, y2: Float) {
        zoom1 = Point(x1, y1)
        zoom2 = Point(x2, y2)
    }
}