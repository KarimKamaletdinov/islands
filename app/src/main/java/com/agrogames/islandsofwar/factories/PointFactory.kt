package com.agrogames.islandsofwar.factories

import com.agrogames.islandsofwar.engine.abs.common.Point
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object PointFactory {
    @Throws(JSONException::class)
    fun points(json: JSONArray): Array<Point?> {
        val result = arrayOfNulls<Point>(json.length())
        for (i in 0 until json.length()) {
            result[i] = point(json.getJSONObject(i))
        }
        return result
    }

    @Throws(JSONException::class)
    fun point(json: JSONObject): Point {
        return Point(json.getDouble("x").toFloat(), json.getDouble("y").toFloat())
    }
}