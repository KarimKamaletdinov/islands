package com.agrogames.islandsofwar.render.impl

import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit
import com.agrogames.islandsofwar.ui.impl.Element
import com.agrogames.islandsofwar.ui.impl.ElementType
import com.agrogames.islandsofwar.ui.impl.UI

class UnitList(private val ui: UI, private val x: Float) {
    private val elements: MutableList<Element> = ArrayList()
    var currentUnit: TransportUnit? = null
        private set

    fun setUnits(units: Array<TransportUnit>) {
        if (currentUnit != null && !units.contains(currentUnit)) currentUnit = null
        for (e in elements) {
            ui.deleteElement(e)
        }
        var y = 7.5f
        for (unit in setOf(*units)) {
            elements.add(ui.createElement(Element(ElementType.Button, x, y, 1.2f, 1.2f, "ui/button_background", {
                if (currentUnit == null || currentUnit !== unit) {
                    clearUnits()
                    currentUnit = unit
                    setUnits(units)
                } else {
                    clearUnits()
                    currentUnit = null
                    setUnits(units)
                }
            })))
            elements.add(ui.createElement(Element(
                ElementType.Button, x, y, 1.2f, 1.2f, TextureMapper.join(unit.example.texture,
                    if (unit == currentUnit) "selected" else "normal"), renderInBorders = false)))
            for (weapon in unit.example.weapons) {
                val e1 = ui.createElement(Element(
                    ElementType.Button, x + weapon.relativeLocation.x, y + weapon.relativeLocation.y, -1f, -1f,
                        TextureMapper.join(weapon.texture)))
                e1.renderInBorders = false
                elements.add(e1)
            }
            y -= 1f
        }
    }

    fun clearUnits(replaceCurrent: Boolean = true) {
        if (replaceCurrent) currentUnit = null
        for (e in elements) {
            ui.deleteElement(e)
        }
    }
}