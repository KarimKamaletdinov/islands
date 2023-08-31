package ru.agrogames.islands.engine.impl

import android.util.Log
import ru.agrogames.islands.engine.abs.GameState
import ru.agrogames.islands.engine.abs.bullet.IBullet
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.graphics.IGraphicsObject
import ru.agrogames.islands.engine.abs.map.MapObject
import ru.agrogames.islands.engine.abs.renderable.RenderableObject
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.impl.bullet.BulletAdder
import ru.agrogames.islands.engine.impl.graphics.GraphicsAdder
import ru.agrogames.islands.engine.impl.map.MapProvider
import ru.agrogames.islands.engine.impl.unit.LandUnit
import ru.agrogames.islands.engine.impl.unit.UnitAdder
import ru.agrogames.islands.factories.Factory
import ru.agrogames.islands.islands.abs.Island
import ru.agrogames.islands.map.Water
import java.util.LinkedList

class Engine(protectors: List<IUnit>, attackers: List<IUnit>, private val mapObjects: Array<MapObject>, val mapName: String) {
    val protectors: MutableList<IUnit> = LinkedList(protectors)
    val attackers: MutableList<IUnit> = LinkedList(attackers)
    private val destroyed: MutableList<IUnit> = LinkedList()
    val protectorsBullets: MutableList<IBullet> = LinkedList()
    val attackersBullets: MutableList<IBullet> = LinkedList()
    private val otherObjects: MutableList<IGraphicsObject> = LinkedList()
    var state = GameState.Game
        private set

    constructor(island: Island) : this(listOf(*island.owners.clone()), listOf(island.attacker), island.map.map, island.map.name)
    fun update(deltaTime: Float) {
        try {
            updateObjects(deltaTime)
            deleteKilled()
            updateState()
        } catch (e: Exception){
            Log.e("IOW", "ERROR IN UPDATE", e)
        }
    }

    private fun updateObjects(deltaTime: Float) {
        val protectorsCopy = protectors.toTypedArray()
        val attackersCopy = attackers.toTypedArray()
        val protectorsBulletsCopy = protectorsBullets.toTypedArray()
        val attackersBulletsCopy = attackersBullets.toTypedArray()
        val graphicsAdder = GraphicsAdder()
        val provider1 = MapProvider(protectorsCopy, attackersCopy, mapObjects)
        val bulletAdder1 = BulletAdder()
        val unitAdder1 = UnitAdder()
        for (unit in protectorsCopy) {
            try {
                unit.update(provider1, bulletAdder1, unitAdder1, graphicsAdder, deltaTime)
            } catch (e: Exception) {
                Log.e("IOW", "An exception has occurred during Update", e)
            }
        }
        for (bullet in protectorsBulletsCopy) {
            try {
                bullet.update(provider1, bulletAdder1, unitAdder1, graphicsAdder, deltaTime)
            } catch (e: Exception) {
                Log.e("IOW", "An exception has occurred during Update", e)
            }
        }
        for (unit in destroyed.toTypedArray()) {
            unit.addTsd(deltaTime)
        }
        for (another in otherObjects.toTypedArray<IGraphicsObject?>()) {
            another!!.update(provider1, bulletAdder1, unitAdder1, graphicsAdder, deltaTime)
        }
        protectorsBullets.addAll(bulletAdder1.bullets)
        protectors.addAll(unitAdder1.units)
        val bulletAdder2 = BulletAdder()
        val provider2 = MapProvider(attackersCopy, protectorsCopy, mapObjects)
        val unitAdder2 = UnitAdder()
        for (unit in attackersCopy) {
            try {
                unit.update(provider2, bulletAdder2, unitAdder2, graphicsAdder, deltaTime)
            } catch (e: Exception) {
                Log.e("IOW", "An exception has occurred during Update", e)
            }
        }
        for (bullet in attackersBulletsCopy) {
            try {
                bullet.update(provider2, bulletAdder2, unitAdder2, graphicsAdder, deltaTime)
            } catch (e: Exception) {
                Log.e("IOW", "An exception has occurred during Update", e)
            }
        }
        attackersBullets.addAll(bulletAdder2.bullets)
        attackers.addAll(unitAdder2.units)
        otherObjects.addAll(graphicsAdder.graphicsObjects)
    }

    private fun deleteKilled() {
        for (unit in protectors.toTypedArray()) {
            if (unit.health.current <= 0) {
                if (unit is LandUnit) {
                    protectors.remove(unit)
                    otherObjects.add(Factory.getGraphics("bang", unit.location, unit.rotation))
                } else {
                    if (!destroyed.contains(unit)) destroyed.add(unit)
                }
            }
        }
        for (unit in attackers.toTypedArray()) {
            if (unit.health.current <= 0) {
                if (unit is LandUnit) {
                    attackers.remove(unit)
                    otherObjects.add(Factory.getGraphics("bang", unit.location, unit.rotation))
                } else {
                    if (!destroyed.contains(unit)) destroyed.add(unit)
                }
            }
        }
        for (unit in destroyed) {
            if (unit.timeSinceDestroyed() >= 3.5f) {
                destroyed.remove(unit)
                attackers.remove(unit)
                if (mapObjects.none { it is Water && it.territory.contains(Cell(unit.location)) }) {
                    otherObjects.add(Factory.getGraphics("big_bang", unit.location, unit.rotation))
                }
                for (cell in unit.territory) {
                    for (u in protectors.filter { it.territory.contains(cell) }) {
                        u.loseHealth(15)
                    }
                    for (u in attackers.filter { it.territory.contains(cell) }) {
                        u.loseHealth(15)
                    }
                }
            }
        }
        for (graphicsObject in otherObjects.toTypedArray()) {
            if (graphicsObject.shouldBeRemoved()) {
                otherObjects.remove(graphicsObject)
            }
        }
        for (bullet in protectorsBullets.toTypedArray()) {
            if (bullet.hasStopped()) {
                protectorsBullets.remove(bullet)
            }
        }
        for (bullet in attackersBullets.toTypedArray()) {
            if (bullet.hasStopped()) {
                attackersBullets.remove(bullet)
            }
        }
    }

    private fun updateState() {
        if (state != GameState.Game) return
        if (attackers.size == 0) state = GameState.Defeat else if (protectors.size == 0) state = GameState.Win
    }

    val other: List<RenderableObject>
        get() {
            val objects: MutableList<RenderableObject> = ArrayList()
            objects.addAll(destroyed)
            objects.addAll(otherObjects)
            return objects
        }


    fun addPlane(plane: IUnit) {
        attackers.add(plane)
    }
}