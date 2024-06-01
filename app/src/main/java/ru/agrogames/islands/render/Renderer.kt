package ru.agrogames.islands.render

import android.util.Pair
import ru.agrogames.islands.common.M
import ru.agrogames.islands.engine.abs.GameState
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.common.Point
import ru.agrogames.islands.engine.abs.movable.MovableObject
import ru.agrogames.islands.engine.abs.transport.Transport
import ru.agrogames.islands.engine.abs.transport.TransportUnit
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.impl.Engine
import ru.agrogames.islands.factories.Factory
import ru.agrogames.islands.graphics.drawtexture.TextureDrawer
import ru.agrogames.islands.map.MapParams
import ru.agrogames.islands.sounds.SoundPlayer
import ru.agrogames.islands.ui.Element
import ru.agrogames.islands.ui.ElementType
import ru.agrogames.islands.ui.UI
import kotlin.math.atan

class Renderer(private val engine: Engine, private val runEngine: Boolean, right: (() -> Unit)?,
               left: (() -> Unit)?, attack: (() -> Unit)?, private var back: (() -> Unit)?, private val cheat: Boolean) {

    private val ui = UI()
    private var selectedUnit: IUnit? = null
    private val selectable: MutableList<Pair<IUnit, Float>> = ArrayList()
    private val cancelButton: Element =
        ui.createElement(Element(ElementType.Button, 14f, 9f, 1f, 1f, "ui/cancel_button",{
            this.visible = false
            landingList.clearUnits()
            selectedUnit = null
        }, false))
    private val landingList: UnitList = UnitList(ui, 14f)
    private val planeList: UnitList = UnitList(ui, 0f)

    init {
        if (runEngine)
            planeList.setUnits(arrayOf(TransportUnit { Factory["bomber", it.x, it.y] }))
        else {
            MapScroller.reset()
            MapScroller.scroll(-7.5f, -5f)
            if (right != null)
                ui.createElement(
                    Element(ElementType.Button, 14f, 1f, 1f, 1f, "ui/right_button",
                    { right() })
                )
            if (left != null)
                ui.createElement(
                    Element(ElementType.Button, 1f, 1f, 1f, 1f, "ui/left_button",
                    { left() })
                )
            if (attack != null)
                ui.createElement(
                    Element(ElementType.Button, 7.5f, 1f, 4f, 1f, "ui/attack_button",
                    { attack() })
                )
        }
    }

    fun render(textureDrawer: TextureDrawer, soundPlayer: SoundPlayer, touchPoint: Point?,
               movePoint: Point?, previousMovePoint: Point?,
               zoom1: Point?, zoom2: Point?, previousZoom1: Point?, previousZoom2: Point?) {
        MapScroller.start(textureDrawer)
        //textureDrawer.drawTexture(15f, 10f, "imagekit/" + engine.mapName.substringBefore('.'), 30f, 20f, 0f)
        for (x in 1 ..<MapParams.width) {
            for (y in 1..<MapParams.height) {
                if(engine.mapObjects.none { it.territory[0] == Cell(x, y) }) {
                    textureDrawer.drawTexture(x.toFloat(), y.toFloat(), "other/land", 1.5f, 1.3f, 0f)
                }
            }
        }
        selectable.clear()
        val renderer = ObjectRenderer(textureDrawer)
        for (r in engine.other) {
            if (r is IUnit) renderer.render(r, "destroyed")
            else renderer.render(r)
        }
        if (selectedUnit != null) {
            if (selectedUnit!!.health.current <= 0) {
                selectedUnit = null
            } else {
                drawRoute(textureDrawer, selectedUnit!!)
                renderer.render(selectedUnit!!, "selected")
                drawHealth(textureDrawer, selectedUnit!!)
            }
        }
        for (unit in engine.attackers.filter { it.height < 3 && it !== selectedUnit }) {
            selectable.add(Pair(unit, renderer.render(unit, "normal")))
        }
        for (unit in engine.protectors) renderer.render(unit, "normal")
        for (bullet in engine.attackersBullets) renderer.render(bullet)
        for (bullet in engine.protectorsBullets) renderer.render(bullet)
        for (weapon in engine.attackers.flatMap { it.weapons.toList() }) renderer.render(weapon)
        for (weapon in engine.protectors.flatMap { it.weapons.toList() }) renderer.render(weapon)
        for (unit in engine.attackers.filter { it.height >= 3 }) renderer.render(unit, "normal")
        MapScroller.finish(textureDrawer)
        if (touchPoint == null) ui.render(textureDrawer, null)
        else if (!ui.render(textureDrawer, touchPoint))
            onTouch(MapScroller.convert(touchPoint), soundPlayer)
        if (movePoint != null && previousMovePoint != null)
            MapScroller.scroll(movePoint.x - previousMovePoint.x, movePoint.y - previousMovePoint.y)
        if (zoom1 != null && zoom2 != null && previousZoom1 != null && previousZoom2 != null)
            MapScroller.zoom(zoom1, zoom2, previousZoom1, previousZoom2)
        if (engine.state == GameState.Win) textureDrawer.drawTexture(7.5f, 5f, "other/win", 0f)
        else if (engine.state == GameState.Defeat) textureDrawer.drawTexture(7.5f, 5f, "other/defeat", 0f)
        if ((engine.state == GameState.Win || engine.state == GameState.Defeat) && back != null)
            ui.createElement(
                Element(ElementType.Button, 7.5f, 1f, 4f, 1f, "ui/back_button",
                {
                    back?.let { it() }
                    back = null
                })
            )
    }

    private fun onTouch(touch: Point, soundPlayer: SoundPlayer) {
        if (!runEngine) return
        val su = selectable.firstOrNull {
            it.first.location.x + it.second!! / 2 > touch.x
                    && it.first.location.x - it.second!! / 2 < touch.x
                    && it.first.location.y + it.second!! / 2 > touch.y
                    && it.first.location.y - it.second!! / 2 < touch.y
                    && it.first is MovableObject
        }
        if (su != null) {
            cancelButton.visible = true
            selectedUnit = su.first
            if (selectedUnit is Transport) landingList.setUnits((selectedUnit as Transport).units.toTypedArray()) else landingList.clearUnits()
        } else if (landingList.currentUnit != null && selectedUnit != null && selectedUnit is Transport) {
            val t = selectedUnit as Transport
            var units = t.units
            if (units.isEmpty()) {
                landingList.clearUnits()
            } else {
                t.spawn(landingList.currentUnit!!, Cell(touch))
                units = t.units
                landingList.clearUnits(false)
                landingList.setUnits(units.toTypedArray())
            }
        } else if (planeList.currentUnit != null) {
            val bomber = planeList.currentUnit!!.create.apply(Cell(-1, -1))
            val mo = bomber as MovableObject
            mo.cleverSetGoal(Cell(Point(touch.x + 1, touch.y + 1)))
            engine.addPlane(bomber)
            if (!cheat) planeList.clearUnits()
        } else if (selectedUnit != null) {
            val mo = selectedUnit as MovableObject
            mo.cleverSetGoal(Cell(touch))
            if (mo.texture == "units/transport_ship") {
                soundPlayer.playSound("waves")
            }
        }
    }

    private fun drawHealth(drawer: TextureDrawer, unit: IUnit) {
        val l = unit.location
        val health = unit.health
        drawer.drawTexture(l.x, l.y + 1, "other/red", health.start / 15f, 0.1f, 0f)
        if (health.current > 0) {
            drawer.drawTexture(l.x - (health.start - health.current) / 30f, l.y + 1, "other/green",
                    health.current / 15f, 0.1f, 0f)
        }
    }

    private fun drawRoute(drawer: TextureDrawer?, unit: IUnit) {
        if (unit is MovableObject) {
            val route = unit.route.toTypedArray()
            if (route.isNotEmpty()) {
                for (i in route.indices.reversed()) {
                    val previous = if (i == route.size - 1) Cell(unit.location) else route[i + 1]
                    val current = route[i]
                    drawLine(drawer, Point(previous), Point(current))
                }
                val finish = Point(route[0])
                drawer!!.drawTexture(finish.x, finish.y, "ui/cross", 1f, 1f, 0f)
            }
        }
    }

    private fun drawLine(drawer: TextureDrawer?, start: Point, finish: Point) {
        val middle = M.middle(start, finish)
        val rotation = atan(
                (start.y.toDouble() - finish.y.toDouble()) /
                        (start.x.toDouble() - finish.x.toDouble())).toFloat()
        drawer!!.drawTexture(middle.x, middle.y, "other/white", M.dist(start, finish), 0.05f, rotation)
    }
}