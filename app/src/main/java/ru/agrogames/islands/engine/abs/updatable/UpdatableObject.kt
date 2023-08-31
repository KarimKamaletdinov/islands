package ru.agrogames.islands.engine.abs.updatable

import ru.agrogames.islands.engine.abs.bullet.BulletAdder
import ru.agrogames.islands.engine.abs.graphics.GraphicsAdder
import ru.agrogames.islands.engine.abs.map.MapProvider
import ru.agrogames.islands.engine.abs.unit.IUnitAdder

interface UpdatableObject {
    fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float)
}