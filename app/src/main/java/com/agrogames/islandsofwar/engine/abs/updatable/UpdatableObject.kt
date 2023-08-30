package com.agrogames.islandsofwar.engine.abs.updatable

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder
import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder
import com.agrogames.islandsofwar.engine.abs.map.MapProvider
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder

interface UpdatableObject {
    fun update(provider: MapProvider, bulletAdder: BulletAdder, unitAdder: IUnitAdder, graphicsAdder: GraphicsAdder, deltaTime: Float)
}