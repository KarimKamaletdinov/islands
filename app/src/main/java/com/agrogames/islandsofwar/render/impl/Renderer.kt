package com.agrogames.islandsofwar.render.impl

import android.util.Pair
import com.agrogames.islandsofwar.common.M
import com.agrogames.islandsofwar.engine.abs.GameState
import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject
import com.agrogames.islandsofwar.engine.abs.transport.Transport
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit
import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import com.agrogames.islandsofwar.engine.impl.Engine
import com.agrogames.islandsofwar.factories.Factory
import com.agrogames.islandsofwar.graphics.drawtexture.TextureDrawer
import com.agrogames.islandsofwar.sounds.SoundPlayer
import com.agrogames.islandsofwar.ui.impl.Element
import com.agrogames.islandsofwar.ui.impl.ElementType
import com.agrogames.islandsofwar.ui.impl.UI
import kotlin.math.atan

class Renderer(private val presenter: Engine, private val runEngine: Boolean, right: (() -> Unit)?,
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
                ui.createElement(Element(ElementType.Button, 14f, 1f, 1f, 1f, "ui/right_button",
                    { right() }))
            if (left != null)
                ui.createElement(Element(ElementType.Button, 1f, 1f, 1f, 1f, "ui/left_button",
                    { left() }))
            if (attack != null)
                ui.createElement(Element(ElementType.Button, 7.5f, 1f, 4f, 1f, "ui/attack_button",
                    { attack() }))
        }
    }

    fun render(drawer: TextureDrawer, soundPlayer: SoundPlayer, touchPoint: Point?,
                        movePoint: Point?, previousMovePoint: Point?,
                        zoom1: Point?, zoom2: Point?, previousZoom1: Point?, previousZoom2: Point?) {
        MapScroller.start(drawer)
        drawer.drawTexture(15f, 10f, "imagekit/map1", 30f, 20f, 0f)
        selectable.clear()
        val renderer = ObjectRenderer(drawer)
        for (r in presenter.other) {
            if (r is IUnit) renderer.render(r, "destroyed")
            else renderer.render(r)
        }
        if (selectedUnit != null) {
            if (selectedUnit!!.health.current <= 0) {
                selectedUnit = null
            } else {
                drawRoute(drawer, selectedUnit!!)
                renderer.render(selectedUnit!!, "selected")
                drawHealth(drawer, selectedUnit!!)
            }
        }
        for (unit in presenter.attackers.filter { it.height < 3 && it !== selectedUnit }) {
            selectable.add(Pair(unit, renderer.render(unit, "normal")))
        }
        for (unit in presenter.protectors) renderer.render(unit, "normal")
        for (bullet in presenter.attackersBullets) renderer.render(bullet)
        for (bullet in presenter.protectorsBullets) renderer.render(bullet)
        for (weapon in presenter.attackers.flatMap { it.weapons.toList() }) renderer.render(weapon)
        for (weapon in presenter.protectors.flatMap { it.weapons.toList() }) renderer.render(weapon)
        for (unit in presenter.attackers.filter { it.height >= 3 }) renderer.render(unit, "normal")
        MapScroller.finish(drawer)
        if (touchPoint == null) ui.render(drawer, null)
        else if (!ui.render(drawer, touchPoint))
            onTouch(MapScroller.convert(touchPoint), soundPlayer)
        if (movePoint != null && previousMovePoint != null)
            MapScroller.scroll(movePoint.x - previousMovePoint.x, movePoint.y - previousMovePoint.y)
        if (zoom1 != null && zoom2 != null && previousZoom1 != null && previousZoom2 != null)
            MapScroller.zoom(zoom1, zoom2, previousZoom1, previousZoom2)
        if (presenter.state == GameState.Win) drawer.drawTexture(7.5f, 5f, "other/win", 0f)
        else if (presenter.state == GameState.Defeat) drawer.drawTexture(7.5f, 5f, "other/defeat", 0f)
        if ((presenter.state == GameState.Win || presenter.state == GameState.Defeat) && back != null)
            ui.createElement(Element(ElementType.Button, 7.5f, 1f, 4f, 1f, "ui/back_button",
                {
                    back?.let { it() }
                    back = null
                }))
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
            presenter.addPlane(bomber)
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