package com.agrogames.islandsofwar.factories

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject
import com.agrogames.islandsofwar.engine.impl.graphics.GraphicsObject
import org.json.JSONObject

object GraphicsFactory {
    val graphics: MutableList<JSONObject> = ArrayList()
    fun getGraphics(name: String, location: Point, rotation: Float): IGraphicsObject =
        graphics(getJSONGraphics(name), location, rotation)

    private fun getJSONGraphics(name: String): JSONObject =
        graphics.find { it.getString("name") == name }!!

    private fun graphics(json: JSONObject, location: Point, rotation: Float): IGraphicsObject =
        if (json.has("next")) {
            GraphicsObject(
                    "graphics/" + json.getString("name"),
                    location,
                    rotation,
                    json.optString("spawn_sound"),
                    json.getDouble("lifetime").toFloat(),
                    getGraphics(json.getString("next"), location, rotation))
        } else GraphicsObject("graphics/" + json.getString("name"),
                location,
                rotation,
                json.optString("spawn_sound"),
            json.getDouble("lifetime").toFloat())
}