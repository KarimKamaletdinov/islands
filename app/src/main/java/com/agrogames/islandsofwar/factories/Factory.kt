package com.agrogames.islandsofwar.factories

import android.content.Context
import android.util.Log
import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject
import com.agrogames.islandsofwar.engine.abs.unit.IUnit
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Scanner

object Factory {
    fun load(context: Context) {
        try {
            var text = Scanner(context.assets.open("units.json")).useDelimiter("\\A").next()
            text = text.replace("\\/", "/")
            val json = JSONObject(text)
            val u = json.getJSONArray("units")
            UnitFactory.units.clear()
            for (i in 0 until u.length()) {
                val unit = u.getJSONObject(i)
                UnitFactory.units.add(unit)
            }
            val w = json.getJSONArray("weapons")
            WeaponFactory.weapons.clear()
            for (i in 0 until w.length()) {
                val weapon = w.getJSONObject(i)
                WeaponFactory.weapons.add(weapon)
            }
            val g = json.getJSONArray("graphics")
            GraphicsFactory.graphics.clear()
            for (i in 0 until g.length()) {
                val gr = g.getJSONObject(i)
                GraphicsFactory.graphics.add(gr)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("IOW", "Unable to load ", e)
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("IOW", "Error in JSON syntax", e)
        }
    }

    operator fun get(name: String, x: Int, y: Int, vararg contentTextures: String): IUnit {
        return UnitFactory.get(name, x, y, *contentTextures)
    }

    fun getGraphics(name: String, location: Point, rotation: Float): IGraphicsObject {
        return GraphicsFactory.getGraphics(name, location, rotation)
    }
}